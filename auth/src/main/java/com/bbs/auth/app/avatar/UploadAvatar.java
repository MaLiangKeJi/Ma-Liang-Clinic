package com.bbs.auth.app.avatar;

import com.bbs.Result;
import com.bbs.auth.entity.User;
import com.bbs.auth.service.UserService;
import com.bbs.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping
@RestController
@Slf4j
public class UploadAvatar {

    @Resource
    private UserService service;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Param {
        private String src;
    }

    /**
     * 头像上传
     */
    @PostMapping("/avatar")
    public Result<Boolean> upload(@RequestBody Param param) {
        UserVO vo = service.loginUser();
        return Result.success(service.lambdaUpdate().set(User::getAvatar, param.src).eq(User::getId, vo.getId()).update());
    }
}
