package com.bbs.auth.app;

import com.bbs.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
public class Test {

    /**
     * 服务启动后的连通性测试
     */
    @GetMapping("/test")
    public Result<Boolean> success() {
        return Result.success();
    }
}
