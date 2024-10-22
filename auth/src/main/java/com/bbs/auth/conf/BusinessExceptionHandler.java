package com.bbs.auth.conf;

import cn.hutool.jwt.JWTException;
import com.bbs.Result;
import com.bbs.exception.ReLoginException;
import com.bbs.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.bbs.enums.CodeEnum.FAILED_LOGIN_PWD_ERROR;
import static com.bbs.enums.CodeEnum.FAILED_USER_LOGIN_EXPIRE;

@Slf4j
@ControllerAdvice
public class BusinessExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public Result<Object> errorHandler(BusinessException exception) {
        exception.printStackTrace();
        return Result.failed(exception.getCode(), exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = { ReLoginException.class, JWTException.class })
    public Result<Object> errorHandler() {
        log.debug("[BusinessExceptionHandler::ReLoginException] FAILED_USER_LOGIN_EXPIRE: code={}; msg={}", FAILED_USER_LOGIN_EXPIRE.getCode(), FAILED_USER_LOGIN_EXPIRE.getCode());
        return Result.failed(FAILED_USER_LOGIN_EXPIRE);
    }

    @ResponseBody
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result<Object> errorHandler(IllegalArgumentException ignoredException) {
        log.debug("[BusinessExceptionHandler::IllegalArgumentException] FAILED_LOGIN_PWD_ERROR: code={}; msg={}", FAILED_LOGIN_PWD_ERROR.getCode(), FAILED_LOGIN_PWD_ERROR.getCode());
        return Result.failed(400, ignoredException.getMessage());
    }

    @ExceptionHandler(value = BeanCreationException.class)
    public ResponseEntity<String> handleConfigLoadFailure(BeanCreationException ex) {
        // 处理配置加载失败的逻辑
        log.error("Nacos 配置加载失败: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Configuration loading failed: " + ex.getMessage());
    }
}
