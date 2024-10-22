package com.bbs.auth.app.user;

import com.bbs.Result;
import com.bbs.auth.converter.UserConverter;
import com.bbs.auth.service.FanService;
import com.bbs.auth.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping
public class Info {


    @Resource
    private UserService service;

    @Resource
    private FanService fanService;

    @Resource
    private UserConverter converter;

    @GetMapping("/user/info")
    public Result<VO> info(@RequestParam Long id) {
        VO vo = converter.toInfoVO(service.search(id));
        vo.fanNumber = fanService.count(id);
        vo.setIsFollow(fanService.isFollow(id));
        return Result.success(vo);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VO {
        /**
         * 用户ID
         */
        private Long id;

        /**
         * 用户名称
         */
        private String name;

        /**
         * 个性签名
         */
        private String sign;

        /**
         * 图片 URL 地址
         */
        private String avatar;

        /**
         * 粉丝数量（关注人数）
         */
        private Long fanNumber;

        /**
         * 登录用户是否关注了当前用户
         */
        private Boolean isFollow;

        /**
         * 个人首页背景图片
         */
        private String backgroundImage;
    }
}
