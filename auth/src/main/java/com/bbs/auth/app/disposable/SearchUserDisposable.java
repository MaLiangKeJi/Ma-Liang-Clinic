package com.bbs.auth.app.disposable;

import com.bbs.Result;
import com.bbs.auth.entity.UserDisposable;
import com.bbs.auth.service.UserDisposableService;
import com.bbs.auth.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping
public class SearchUserDisposable {

    @Resource
    private UserDisposableService userDisposableService;

    @Resource
    private UserService userService;

    @GetMapping("/disposable")
    public Result<Boolean> search(@RequestParam String code) {
        return Result.success(userDisposableService.lambdaQuery()
                .eq(UserDisposable::getCode, code)
                .eq(UserDisposable::getUserId, userService.loginUser().getId())
                .exists()
        );
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class FinishedParam {

        private String code;
    }

    @PutMapping("/disposable")
    public Result<Boolean> finished(@RequestBody FinishedParam param) {
        return Result.success(userDisposableService.save(new UserDisposable(userService.loginUser().getId(), param.getCode())));
    }
}
