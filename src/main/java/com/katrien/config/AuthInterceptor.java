package com.katrien.config;

import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthInterceptor implements HandlerInterceptor {

    private void sendUnauthorizedResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);  // 修改这里，返回200而不是401
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"message\":\"未登录\"}");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 允许跨域
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:5173");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Expose-Headers", "Authorization");  // 允许前端访问Authorization头
        
        // 如果是OPTIONS请求，直接放行
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        // 打印请求信息
        System.out.println("=== Auth Debug Info ===");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("Authorization Header: " + request.getHeader("Authorization"));

        // 从请求头中获取token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7).trim();  // 添加 trim() 去除可能的空格
            System.out.println("Parsed token: " + token);
            // 验证token格式是否正确（这里假设token格式为"user_数字"）
            if (token.matches("^user_\\d+$")) {  // 添加 ^ 和 $ 确保完全匹配
                // 打印成功信息
                System.out.println("Token validation successful");
                return true;
            }
            System.out.println("Token format invalid: " + token);
        } else {
            System.out.println("No valid Authorization header found");
        }

        // 如果没有token或token无效，返回401未授权
        sendUnauthorizedResponse(response);
        return false;
    }
}
