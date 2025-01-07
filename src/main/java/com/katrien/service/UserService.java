package com.katrien.service;

import com.katrien.pojo.User;
import java.util.List;

public interface UserService {
    User getUserById(Integer userId);
    User getUserByUsername(String username);
    List<User> getAllUsers();
    boolean createUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(Integer userId);
    User login(String username, String password);
    boolean verifyAndResetPassword(Integer userId, String oldPassword, String newPassword);
    List<User> searchUsers(String keyword, String role);
}