package com.clinic.controller;

import com.bbs.Result;
import com.clinic.converter.FeedbackConverter;
import com.clinic.entity.Feedback;
import com.clinic.service.FeedbackService;
import com.clinic.util.LoginUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * 用户意见与反馈控制器
 */
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Resource
    private FeedbackConverter feedConverter;

    @Resource
    private FeedbackService feedService;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddFeedbackParam {

        /**
         * 内容
         */
        @NotNull(message = "内容不能为空！")
        private String feedback;

        /**
         * 手机号
         */
        private Long phone;
    }

    @PutMapping
    public Result<Boolean> addPatient(@RequestBody @Valid AddFeedbackParam param) {
        Feedback feed = feedConverter.toEntity(param);

        boolean hasPhone = Objects.nonNull(param.getPhone());
        Long userId = LoginUser.getId();
        if (!hasPhone) {
            if (Objects.nonNull(userId)) {
                feed.setUserId(userId);
                feed.setPhone(LoginUser.get().getPhone());
            }
        } else {
            if (Objects.nonNull(userId))
                feed.setUserId(userId);
        }

        return Result.success(feedService.save(feed));
    }
}