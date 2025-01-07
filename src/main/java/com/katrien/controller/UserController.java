package com.katrien.controller;

import com.katrien.common.Result;
import com.katrien.pojo.User;
import com.katrien.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true", maxAge = 3600)
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public Result<User> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error(404, "用户不存在");
        }
    }

    @GetMapping
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return Result.success(users);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        try {
            if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                    user.getPassword() == null || user.getPassword().trim().isEmpty() ||
                    user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                response.put("code", 400);
                response.put("message", "用户名、密码和邮箱不能为空");
                return ResponseEntity.badRequest().body(response);
            }

            if (userService.getUserByUsername(user.getUsername()) != null) {
                response.put("code", 400);
                response.put("message", "用户名已存在");
                return ResponseEntity.badRequest().body(response);
            }

            if (user.getRole() == null) {
                user.setRole("club_president");
            }
            if (user.getRealName() == null) {
                user.setRealName(user.getUsername());
            }
            if (user.getPhone() == null) {
                user.setPhone("未设置");
            }

            // 创建用户
            if (userService.createUser(user)) {
                response.put("code", 200);
                response.put("message", "注册成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("code", 500);
                response.put("message", "注册失败，请检查邮箱格式是否正确");
                return ResponseEntity.status(500).body(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("code", 500);
            response.put("message", "注册失败：" + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");

        Map<String, Object> response = new HashMap<>();

        if (username == null || password == null ||
                username.trim().isEmpty() || password.trim().isEmpty()) {
            response.put("code", 400);
            response.put("message", "用户名和密码不能为空");
            return ResponseEntity.badRequest().body(response);
        }

        User user = userService.login(username, password);

        if (user != null) {
            user.setPasswordHash(null);
            String token = "user_" + user.getUserId();
            response.put("code", 200);
            response.put("message", "登录成功");
            response.put("data", user);
            response.put("token", token);

            return ResponseEntity.ok()
                    .header("Authorization", "Bearer " + token)
                    .body(response);
        } else {
            response.put("code", 401);
            response.put("message", "用户名或密码错误");
            return ResponseEntity.status(401).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(@PathVariable Integer id, @RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        user.setUserId(id);

        if (userService.updateUser(user)) {
            response.put("code", 200);
            response.put("message", "更新成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 404);
            response.put("message", "用户不存在");
            return ResponseEntity.status(404).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Integer id) {
        Map<String, Object> response = new HashMap<>();

        if (userService.deleteUser(id)) {
            response.put("code", 200);
            response.put("message", "删除成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 404);
            response.put("message", "用户不存在");
            return ResponseEntity.status(404).body(response);
        }
    }

    @PutMapping("/{userId}/password")
    public ResponseEntity<Map<String, Object>> updatePassword(
            @PathVariable Integer userId,
            @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();

        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        if (oldPassword == null || newPassword == null ||
                oldPassword.trim().isEmpty() || newPassword.trim().isEmpty()) {
            response.put("code", 400);
            response.put("message", "新密码和原密码不能为空");
            return ResponseEntity.badRequest().body(response);
        }

        if (userService.verifyAndResetPassword(userId, oldPassword, newPassword)) {
            response.put("code", 200);
            response.put("message", "密码重置成功");
            return ResponseEntity.ok(response);
        } else {
            response.put("code", 400);
            response.put("message", "原密码验证失败");
            return ResponseEntity.status(400).body(response);
        }
    }
}
