package com.katrien.common;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
/**
 * @author : Katrien
 * 全局异常处理器，用于统一处理应用程序中可能出现的各种异常
 */
//复合注解：@ControllerAdvice + @ResponseBody
//专门用于构建RESTful控制器的全局异常处理器
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * @author : Katrien
     * @description :处理所有未被其他更具体的异常处理器捕获的异常
     */
    @ExceptionHandler(Exception.class)
    //异常处理方法
    public Result<Void> handleException(Exception e) {
        e.printStackTrace();
        return Result.error("服务器内部错误");
    }

    //处理重复键异常
    @ExceptionHandler(DuplicateKeyException.class)
    public Result<Void> handleDuplicateKeyException(DuplicateKeyException e) {
        return Result.error("数据已存在");
    }

    //处理参数验证异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValidationException(MethodArgumentNotValidException e) {
        return Result.error(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    //处理非法参数异常
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return Result.error(e.getMessage());
    }

    //处理访问被拒绝异常
    @ExceptionHandler(AccessDeniedException.class)
    public Result<Void> handleAccessDeniedException(AccessDeniedException e) {
        return Result.error(403, "没有权限执行此操作");
    }

    //处理认证异常
    @ExceptionHandler(AuthenticationException.class)
    public Result<Void> handleAuthenticationException(AuthenticationException e) {
        return Result.error(401, "认证失败");
    }
}
