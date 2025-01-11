package com.katrien.service.impl;

import com.katrien.mapper.UserMapper;
import com.katrien.pojo.User;
import com.katrien.pojo.Club;
import com.katrien.pojo.Member;
import com.katrien.service.UserService;
import com.katrien.service.ClubService;
import com.katrien.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ClubService clubService;

    @Autowired
    private MemberService memberService;

    @Override
    public User getUserById(Integer userId) {
        return userMapper.getUserById(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }

    @Override
    @Transactional
    public boolean createUser(User user) {
        try {
            // 设置默认值，避免空值
            if (user.getRealName() == null) {
                user.setRealName(user.getUsername());
            }
            if (user.getPhone() == null) {
                user.setPhone("未设置");
            }
            if (user.getRole() == null) {
                user.setRole("club_president");
            }

            // 验证邮箱格式
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                return false;
            }

            // 验证邮箱格式
            if ("club_president".equals(user.getRole())) {
                // 社长必须使用学校邮箱：8位数字@czu.cn
                if (!user.getEmail().matches("^\\d{8}@czu\\.cn$")) {
                    return false;
                }

                // 验证社团ID
                Integer clubId = user.getClubId();
                if (clubId == null) {
                    // 注册时可以不指定社团
                    user.setClubId(null);
                } else {
                    // 检查社团是否已有社长
                    User existingPresident = userMapper.getClubPresident(clubId);
                    if (existingPresident != null) {
                        return false; // 该社团已有社长
                    }
                }
            } else {
                // 管理员使用普通邮箱格式验证
                if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    return false;
                }
                // 管理员不需要关联社团
                user.setClubId(null);
            }

            // 使用password字段进行加密
            String rawPassword = user.getPassword();
            if (rawPassword == null || rawPassword.trim().isEmpty()) {
                return false;
            }
            user.setPasswordHash(BCrypt.hashpw(rawPassword, BCrypt.gensalt()));
            user.setCreatedAt(LocalDateTime.now());

            // 创建用户
            if (userMapper.createUser(user) <= 0) {
                return false;
            }

            // 如果是社长且指定了社团，更新社团的president_id
            if ("club_president".equals(user.getRole()) && user.getClubId() != null) {
                Club club = clubService.getClubById(user.getClubId());
                if (club != null) {
                    club.setPresidentId(user.getUserId());
                    clubService.updateClub(club);

                    // 将社长添加到社团成员名单
                    Member member = new Member();
                    member.setClubId(user.getClubId());
                    member.setName(user.getRealName());
                    // 从邮箱中提取学号（前8位数字）
                    String email = user.getEmail();
                    String studentId = email.substring(0, email.indexOf('@'));
                    member.setStudentId(studentId);
                    member.setRole("社长");
                    member.setJoinDate(LocalDate.now());
                    member.setContactInfo(user.getPhone());
                    member.setStatus("active");

                    if (!memberService.createMember(member)) {
                        throw new RuntimeException("Failed to create member record for club president");
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateUser(User user) {
        // 检查用户是否存在
        User existingUser = userMapper.getUserById(user.getUserId());
        if (existingUser == null) {
            return false;
        }

        // 如果用户名被修改，检查新用户名是否已存在
        if (!existingUser.getUsername().equals(user.getUsername())) {
            User userWithSameUsername = userMapper.getUserByUsername(user.getUsername());
            if (userWithSameUsername != null && !userWithSameUsername.getUserId().equals(user.getUserId())) {
                return false;
            }
        }

        // 保持密码和角色不变
        user.setPasswordHash(existingUser.getPasswordHash());
        user.setRole(existingUser.getRole());

        int result = userMapper.updateUser(user);
        return result > 0;
    }

    @Override
    public boolean deleteUser(Integer userId) {
        return userMapper.deleteUser(userId) > 0;
    }

    @Override
    public User login(String username, String password) {
        User user = userMapper.getUserByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPasswordHash())) {
            // 更新最后登录时间
            user.setLastLoginAt(LocalDateTime.now());
            userMapper.updateUser(user);

            if ("admin".equalsIgnoreCase(user.getRole())) {
                user.setRole("admin");
            } else if ("club_president".equalsIgnoreCase(user.getRole())) {
                user.setRole("club_president");
            }

            return user;
        }
        return null;
    }

    @Override
    public boolean verifyAndResetPassword(Integer userId, String oldPassword, String newPassword) {
        User user = userMapper.getUserById(userId);
        if (user != null && BCrypt.checkpw(oldPassword, user.getPasswordHash())) {
            // 旧密码验证成功，设置新密码
            String newHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            return userMapper.updatePassword(userId, newHash) > 0;
        }
        return false;
    }

    @Override
    public List<User> searchUsers(String keyword, String role) {
        return userMapper.searchUsers(keyword, role);
    }
}