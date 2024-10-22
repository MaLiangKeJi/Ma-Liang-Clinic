package com.bbs.auth.app.logout;

import com.bbs.Result;
import com.bbs.auth.cache.BindLoginCompanyCache;
import com.bbs.auth.service.TokenService;
import com.bbs.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping
public class Logout {

    @Resource
    private TokenService tokenService;
    @Resource
    private BindLoginCompanyCache bindLoginCompanyCache;

    @DeleteMapping
    public Result<Boolean> logout(HttpServletRequest request) {
        String token = tokenService.getToken(request);
        log.debug("[UserLogout::logout] token={}", token);
        if(StringUtils.isNotBlank(token) && tokenService.verifyToken(token)) {
            UserVO userVO = tokenService.parseToken(token);
            tokenService.clearLoginFlag(userVO.getId());
            bindLoginCompanyCache.remove(userVO.getId());
            return Result.success();
        }
        return Result.failed(500, "账号登出失败，请联系客服！");
    }

}
