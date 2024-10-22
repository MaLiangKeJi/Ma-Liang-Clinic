package com.bbs.auth.app.fan;

import cn.hutool.core.collection.CollUtil;
import com.bbs.Result;
import com.bbs.auth.entity.Fan;
import com.bbs.auth.service.FanService;
import com.bbs.auth.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.List;

public class GetFan {

    @Resource
    private FanService fanService;
    @Resource
    private UserService userService;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DTO {

        private Long userId;

        private String userName;

        private String url;

    }

    /**
     * 查询粉丝
     * @return Boolean
     */
    @GetMapping("/fan")
    public Result<List<DTO>> getFan(){
        List<Fan> list = fanService.getFollow(userService.loginUser().getId());
        if(CollUtil.isNotEmpty(list)){
//            List<GetFollowOrFanDto> result = list.stream().map(o -> new GetFollowOrFanDto(o.getUserId(), , )).collect(Collectors.toList());
//            return Result.success(result);
        }
        return Result.failedNull();
    }
}
