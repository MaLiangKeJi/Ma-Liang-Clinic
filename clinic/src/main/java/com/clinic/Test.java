package com.clinic;

import com.bbs.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class Test {

    @GetMapping("/test")
    public Result<Boolean> test() {
        return Result.success();
    }
}
