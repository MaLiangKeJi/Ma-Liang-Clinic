package com.bbs.auth.app.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.bbs.auth.entity.System;
import com.bbs.auth.enums.SystemState;
import com.bbs.auth.service.SystemService;
import com.bbs.vo.UserVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping
public class SearchSystem {

    @Resource
    private SystemService service;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VO {
        /**
         * 主键
         */
        @TableId(value = "id", type = IdType.AUTO)
        private Long id;

        /**
         * 系统名称
         */
        private String name;

        /**
         * 管理员ID
         */
        private UserVO admin;

        /**
         * 系统描述
         */
        private String description;

        /**
         * 父级系统 ID（0为最大级别）
         */
        private VO pid;

        /**
         * 创建时间
         */
        private Date createTime;

        /**
         * 创建人
         */
        private UserVO createBy;

        /**
         * 修改时间
         */
        private Date updateTime;

        /**
         * 修改人
         */
        private UserVO updateBy;

        /**
         * 系统状态（0正常）
         */
        private SystemState state;
    }

    @GetMapping("/system/list")
    public Result<Page<System>> search(@RequestParam Integer current, @RequestParam Integer size) {
        return Result.success(service.lambdaQuery()
                .eq(System::getState, NumberUtils.INTEGER_ZERO)
                .page(new Page<>(current, size))
        );
    }
}
