package com.katrien.controller;

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
    public ResponseEntity<Map<String, Object>> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        if (user != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("code", 200);
            response.put("message", "获取用户信息成功");
            response.put("data", user);
            return ResponseEntity.ok(response);
        }
        Map<String, Object> error = new HashMap<>();
        error.put("code", 404);
        error.put("message", "用户不存在");
        return ResponseEntity.status(404).body(error);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String role) {
        List<User> users;
        if ((keyword != null && !keyword.isEmpty()) || (role != null && !role.isEmpty())) {
            users = userService.searchUsers(keyword, role);
        } else {
            users = userService.getAllUsers();
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", 200);
        response.put("message", "获取用户列表成功");
        response.put("data", users);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 基本参数验证
            if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
                user.getPassword() == null || user.getPassword().trim().isEmpty() ||
                user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                response.put("code", 400);
                response.put("message", "用户名、密码和邮箱不能为空");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 检查用户名是否已存在
            if (userService.getUserByUsername(user.getUsername()) != null) {
                response.put("code", 400);
                response.put("message", "用户名已存在");
                return ResponseEntity.badRequest().body(response);
            }
            
            // 设置默认值
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
        System.err.println("=============== Controller Debug Info ===============");
        System.err.println("Raw request body: " + loginRequest);
        
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        System.err.println("Parsed username: " + username);
        System.err.println("Parsed password: " + password);
        
        Map<String, Object> response = new HashMap<>();
        
        if (username == null || password == null ||
            username.trim().isEmpty() || password.trim().isEmpty()) {
            System.err.println("Username or password is empty");
            System.err.println("=================================================");
            response.put("code", 400);
            response.put("message", "用户名和密码不能为空");
            return ResponseEntity.badRequest().body(response);
        }
    
        User user = userService.login(username, password);
        System.err.println("Login service returned: " + (user != null ? "success" : "failed"));
        System.err.println("=================================================");
        
        if (user != null) {
            user.setPasswordHash(null);  // 不返回密码哈希
            
            // 生成简单的 token（格式：user_用户ID）
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
        
        System.err.println("=============== Update User Debug Info ===============");
        System.err.println("User ID: " + id);
        System.err.println("Request Body: " + user);
        System.err.println("Username: " + user.getUsername());
        System.err.println("Real Name: " + user.getRealName());
        System.err.println("Email: " + user.getEmail());
        System.err.println("Phone: " + user.getPhone());
        
        if (userService.updateUser(user)) {
            System.err.println("Update successful");
            response.put("code", 200);
            response.put("message", "更新成功");
            return ResponseEntity.ok(response);
        } else {
            System.err.println("Update failed");
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
    public ResponseEntity<Map<String, Object>> updatePassword(  // 改名为 updatePassword
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

    // 内部类用于登录请求
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
