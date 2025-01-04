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
                System.err.println("Email is empty");
                return false;
            }
            
            // 验证邮箱格式
            if ("club_president".equals(user.getRole())) {
                // 社长必须使用学校邮箱：8位数字@czu.cn
                if (!user.getEmail().matches("^\\d{8}@czu\\.cn$")) {
                    System.err.println("Invalid email format for club president: " + user.getEmail());
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
                        System.err.println("Club already has a president: " + clubId);
                        return false; // 该社团已有社长
                    }
                }
            } else {
                // 管理员使用普通邮箱格式验证
                if (!user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                    System.err.println("Invalid email format: " + user.getEmail());
                    return false;
                }
                // 管理员不需要关联社团
                user.setClubId(null);
            }
            
            // 使用password字段进行加密
            String rawPassword = user.getPassword();
            if (rawPassword == null || rawPassword.trim().isEmpty()) {
                System.err.println("Password is empty");
                return false;
            }
            user.setPasswordHash(BCrypt.hashpw(rawPassword, BCrypt.gensalt()));
            user.setCreatedAt(LocalDateTime.now());
            
            // 创建用户
            System.out.println("Creating user with data: " + user);
            if (userMapper.createUser(user) <= 0) {
                System.err.println("Failed to create user");
                return false;
            }
            System.out.println("User created successfully with ID: " + user.getUserId());
            
            // 如果是社长且指定了社团，更新社团的president_id
            if ("club_president".equals(user.getRole()) && user.getClubId() != null) {
                System.out.println("Updating club president_id for club: " + user.getClubId());
                Club club = clubService.getClubById(user.getClubId());
                if (club != null) {
                    System.out.println("Found club: " + club);
                    club.setPresidentId(user.getUserId());
                    int result = clubService.updateClub(club);
                    System.out.println("Club update result: " + result);
                    
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
                        System.err.println("Failed to create member record for club president");
                        throw new RuntimeException("Failed to create member record for club president");
                    }
                } else {
                    System.err.println("Club not found with ID: " + user.getClubId());
                }
            }
            
            return true;
        } catch (Exception e) {
            System.err.println("Error creating user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateUser(User user) {
        System.err.println("=============== Update User Service Debug Info ===============");
        System.err.println("Updating user with ID: " + user.getUserId());
        System.err.println("New username: " + user.getUsername());
        System.err.println("New real name: " + user.getRealName());
        System.err.println("New email: " + user.getEmail());
        System.err.println("New phone: " + user.getPhone());
        
        // 检查用户是否存在
        User existingUser = userMapper.getUserById(user.getUserId());
        if (existingUser == null) {
            System.err.println("User not found with ID: " + user.getUserId());
            return false;
        }
        
        // 如果用户名被修改，检查新用户名是否已存在
        if (!existingUser.getUsername().equals(user.getUsername())) {
            User userWithSameUsername = userMapper.getUserByUsername(user.getUsername());
            if (userWithSameUsername != null && !userWithSameUsername.getUserId().equals(user.getUserId())) {
                System.err.println("Username already exists: " + user.getUsername());
                return false;
            }
        }
        
        // 保持密码和角色不变
        user.setPasswordHash(existingUser.getPasswordHash());
        user.setRole(existingUser.getRole());
        
        int result = userMapper.updateUser(user);
        System.err.println("Update result: " + result);
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
            
            // 将数据库中的角色名转换为前端使用的格式
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
    public boolean resetPassword(Integer userId, String newPassword) {
        User user = userMapper.getUserById(userId);
        if (user != null && newPassword != null && !newPassword.trim().isEmpty()) {
            String newHash = BCrypt.hashpw(newPassword, BCrypt.gensalt());
            System.err.println("Resetting password for user: " + userId);
            System.err.println("New password hash: " + newHash);
            return userMapper.updatePassword(userId, newHash) > 0;
        }
        return false;
    }
}
