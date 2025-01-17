package com.katrien.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author : Katrien
 * @description : 定义了一个认证拦截器，用于验证用户是否已登录
 * @description :实现了HandlerInterceptor接口，用于在Spring MVC请求处理过程中拦截请求
 *
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    // 配置的 token 前缀
    private static final String BEARER_PREFIX = "Bearer ";
    // 配置的 token 格式
    private static final String TOKEN_PATTERN = "^user_\\d+$";

    @Override
    // 重写 preHandle 方法，用于拦截请求
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取请求头中的 Authorization
        String authHeader = request.getHeader("Authorization");

        // 检查 Authorization 头部
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            sendErrorResponse(response, "Missing or invalid Authorization header");
            return false;
        }

        // 获取 token
        String token = authHeader.substring(BEARER_PREFIX.length());

        // 验证 token
        if (!isValidToken(token)) {
            sendErrorResponse(response, "Invalid token");
            return false;
        }
        // Token 验证成功
        return true;
    }

    private boolean isValidToken(String token) {
        // TODO: 实现更复杂的 token 验证逻辑
        return token.matches(TOKEN_PATTERN);
    }

    // 发送错误响应
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"error\": \"%s\"}", message));
    }
}