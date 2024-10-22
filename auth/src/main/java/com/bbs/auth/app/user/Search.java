package com.bbs.auth.app.user;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.bbs.auth.converter.UserConverter;
import com.bbs.auth.entity.Fan;
import com.bbs.auth.entity.User;
import com.bbs.auth.service.FanService;
import com.bbs.auth.service.UserService;
import com.bbs.util.PhoneUtil;
import com.bbs.vo.UserVO;
import com.bbs.exception.ReLoginException;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.bbs.Result.success;
import static java.util.Objects.nonNull;

@Slf4j
@RestController
@RequestMapping
public class Search {

    @Resource
    private UserService service;
    @Resource
    private UserConverter converter;
    @Resource
    private FanService fanService;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VO {

        /**
         * 用户 ID
         */
        private Long id;

        /**
         * 用户昵称
         */
        private String name;

        /**
         * 头像 URL 地址
         */
        private String avatar;

        /**
         * 当前用户是否关注了该 vo 用户
         */
        private Boolean isFollow;
    }

    @GetMapping("/list")
    public Result<Page<VO>> search(
            @RequestParam("val") String val,
            @RequestParam(name = "current", required = false) Integer current,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "fan", required = false, defaultValue = "true") Boolean fan
    ) throws ReLoginException {
        Long numVal = null;
        if(PhoneUtil.isNumber(val)) {
            numVal = Long.parseLong(val);
        }
        UserVO loginUser = service.loginUser();
        Page<User> page;
        if(fan) {
            page = service.selectJoinListPage(new Page<>(current, size), User.class, new MPJLambdaWrapper<User>()
                    .selectAll(User.class)
                    .selectAssociation(Fan.class, User::getFan)
                    .leftJoin(Fan.class, on -> on
                            .eq(Fan::getDeleteFlag, NumberUtils.INTEGER_ZERO)
                            .eq(Fan::getUserId, loginUser.getId())
                            .eq(Fan::getFollowUserId, User::getId)
                    )
                    .like(!PhoneUtil.isNumber(val), User::getName, val)
                    .eq(nonNull(numVal), User::getPhone, numVal)
                    .or(nonNull(numVal))
                    .eq(nonNull(numVal), User::getId, numVal)
                    .ne(User::getId, loginUser.getId())
            );
        } else {
            page = service.lambdaQuery()
                    .like(!PhoneUtil.isNumber(val), User::getName, val)
                    .eq(nonNull(numVal), User::getPhone, numVal)
                    .or(nonNull(numVal))
                    .eq(nonNull(numVal), User::getId, numVal)
                    .ne(User::getId, loginUser.getId())
                    .page(new Page<>(current, size));
        }
        List<VO> vos = page.getRecords().stream().map(user -> {
            VO vo = converter.toSearchUserVO(user);
            vo.setIsFollow(nonNull(user.getFan()));
            return vo;
        }).collect(Collectors.toList());
        log.debug("[Search::search] loginUser={}; page={}; vos={}", loginUser, JSONUtil.toJsonStr(page), vos);
        return success(new Page<VO>(page.getCurrent(), page.getSize(), page.getTotal()).setRecords(vos));
    }

    @GetMapping("/server/list")
    public Result<List<VO>> search(
            @RequestParam("ids") List<Long> ids
    ) {
        List<VO> vos = converter.toSearchUserVO(service.search(ids));
        if(service.isLogin()) fanService.fillFollowStatus(ids, vos);
        return success(vos);
    }


    @GetMapping("/server/{id}")
    public Result<VO> search(@PathVariable(value="id") Long id) {
        return success(converter.toSearchUserVO(service.search(id)));
    }
}
