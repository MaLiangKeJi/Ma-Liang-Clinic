package com.bbs.auth.app.user;

import cn.hutool.core.date.DateUtil;
import cn.hutool.jwt.JWTException;
import com.bbs.Result;
import com.bbs.auth.converter.UserConverter;
import com.bbs.auth.entity.User;
import com.bbs.auth.entity.UserCompany;
import com.bbs.auth.service.TokenService;
import com.bbs.auth.service.UserService;
import com.bbs.exception.ReLoginException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

import static com.bbs.Result.success;

@RestController
@RequestMapping
public class Me {

    @Resource
    private TokenService tokenService;

    @Resource
    private UserService service;

    @Resource
    private UserConverter converter;

    /**
     * 当前用户个人信息（不响应 401）
     */
    @GetMapping("/me")
    public Result<VO> me() throws ReLoginException {
        VO vo = new VO();
        try {
            Long uid = service.loginUser().getId();
            User user = service.search(uid);
            vo = converter.toMeVO(user);
            vo.setExpirationDate(DateUtil.format(user.getExpirationTime(), "yyyy-MM-dd"));
            vo.setLoginState(true);
            return success(vo);
        } catch (ReLoginException | JWTException e) {
            vo.setLoginState(false);
            return success(vo);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VO {

        private boolean loginState;

        private Long id;

        private String name;

        private String email;

        private String phone;

        private String idCard;

        private Integer sex;

        /**
         * 个性签名
         */
        private String sign;

        /**
         * 账号状态
         */
        private Integer state;

        /**
         * 图片 URL 地址
         */
        private String avatar;

        /**
         * 平台角色
         */
        private Integer paasRole;

        /**
         * 用户公司
         */
        List<UserCompany> userCompanyList;

        /**
         * 过期时间
         */
        private String expirationDate;
    }
}
