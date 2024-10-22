package com.clinic.conf;

import com.bbs.Result;
import com.bbs.exception.BusinessException;
import com.bbs.exception.ReLoginException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@ControllerAdvice
public class BusinessExceptionHandler {

    @Resource
    private HttpServletResponse response;

    @ResponseBody
    @ExceptionHandler(value = BusinessException.class)
    public Result<Object> errorHandler(BusinessException exception) {
        return Result.failed(exception.getCode(), exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = ReLoginException.class)
    public void errorHandler(ReLoginException exception) throws IOException {
        response.sendError(exception.getCode(), exception.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = IllegalArgumentException.class)
    public void errorHandler(IllegalArgumentException exception) throws IOException {
        String message = exception.getMessage();
        log.debug("[ExceptionHandler::IllegalArgumentException] error={}", message);
        response.sendError(400, message);
    }
}
