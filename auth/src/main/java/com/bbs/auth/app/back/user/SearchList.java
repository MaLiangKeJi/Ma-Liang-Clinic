package com.bbs.auth.app.back.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.bbs.auth.entity.User;
import com.bbs.auth.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.bbs.auth.enums.BackSearchUserTypes.*;

@RequestMapping
@RestController("backSearchUser")
public class SearchList {

    @Resource
    private UserService userService;

    @GetMapping("/back/user/list")
    public Result<Page<User>> search(
            @RequestParam(required = false) String type,
            @RequestParam(required = false, defaultValue = "1") Integer current,
            @RequestParam(required = false, defaultValue = "10") Integer size
    ) {
        userService.checkLoginUserIsAdmin();
        if(StringUtils.isBlank(type) || ALL.getCode().equals(type)) {
            return Result.success(userService.page(new Page<>(current, size)));
        } else if(ON_LINE.getCode().equals(type)) {

        }
        return null;
    }
}
