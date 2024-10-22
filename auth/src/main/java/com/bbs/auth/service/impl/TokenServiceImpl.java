package com.bbs.auth.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWTUtil;
import com.bbs.auth.cache.TokenCache;
import com.bbs.auth.cache.user.UserCache;
import com.bbs.auth.entity.User;
import com.bbs.auth.service.TokenService;
import com.bbs.exception.ReLoginException;
import com.bbs.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static cn.hutool.core.bean.BeanUtil.toBean;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.apache.commons.lang3.math.NumberUtils.*;

@Slf4j
@Service
@RefreshScope
public class TokenServiceImpl implements TokenService {


    @Value("${jwt.token}")
    private String key;

    //token过期时间阈值
    @Value("${jwt.expireTime}")
    private Integer expireTime;

    @Value("${jwt.name}")
    private String tokenName;

    @Resource
    private TokenCache cache;

    @Resource
    private UserCache userCache;

    @Resource(name = "protoStuffTemplate")
    private RedisTemplate<String, String> redisTemplate;

    public static final String LOGIN_TOKEN_PREFIX = "AUTH_LOGIN_TOKEN_";

    /**
     * JWTUtil生成token
     * @param user 入参
     * @return token
     */
    @Override
    public String createToken(User user) {
        return JWTUtil.createToken(createJWTTokeParam(user), key.getBytes());
    }

    private Map<String, Object> createJWTTokeParam(User user) {
        Map<String, Object> map = new HashMap<>();
        Date date = DateUtil.parse(DateUtil.now());
        Date failureTokenTime = DateUtil.offsetDay(date, + expireTime);
        map.put("id", user.getId());
        map.put("name",user.getName());
        map.put("failureTokenTime",failureTokenTime.getTime());
        return map;
    }

    @Override
    public void setLoginFlag(Long uid) {
        setLoginFlag(uid, expireTime, TimeUnit.DAYS);
    }

    @Override
    public void setLoginFlag(Long uid, Integer expireNumber, TimeUnit expireUnit) {
        if(isNull(expireNumber)) expireNumber = expireTime;
        redisTemplate.opsForValue().set(getTokenKey(uid), DateUtil.now(), expireNumber, TimeUnit.DAYS);
    }


    @Override
    public String getLoginFlag(Long uid) {
        return redisTemplate.opsForValue().get(getTokenKey(uid));
    }

    @Override
    public void clearLoginFlag(Long uid) {
        redisTemplate.delete(getTokenKey(uid));
    }

    @Override
    public String getToken(HttpServletRequest request) throws ReLoginException {
        String token = request.getHeader(tokenName);
        if(StringUtils.isBlank(token)) throw new ReLoginException();
        return token.split(" ")[1];
    }

    @Override
    public String verifyAndExpireToken(User user) {
        String token = cache.getToken(user.getId());
        if(Strings.isNotBlank(token)) {
            UserVO vo = verify(token);
            cache.expireToken(vo.getId());
        } else {
            token = createToken(user);
            cache.setToken(user.getId(), token);
        }
        return token;
    }

    @Override
    public String getTokenKey(Long uid) {
        return LOGIN_TOKEN_PREFIX + uid;
    }

    private static final String TOKEN_PREFIX = " ";

    @Override
    public UserVO verify(String token) throws ReLoginException {
        if(StringUtils.isBlank(token)) {
            throw new ReLoginException();
        }
        String[] arr = token.split(TOKEN_PREFIX);
        // 前端 token 可能以【前缀 token】的格式，这里以空格为分隔符，尝试判断获取实际的 token 部分
        String tokenPart = arr.length == INTEGER_TWO ? arr[INTEGER_ONE] : arr[INTEGER_ZERO];
        if(verifyToken(tokenPart)) {
            Long id = parseToken(tokenPart).getId();
            if(nonNull(getLoginFlag(id))) {
                try {
                    User user = userCache.search(id);
                    return new UserVO(user.getId(), user.getName(), user.getEmail(), user.getPhone().toString());
                } catch (InterruptedException | IllegalArgumentException e) {
                    throw new ReLoginException();
                }
            }
        }
        throw new ReLoginException();
    }

    @Override
    public UserVO parseToken(String token) {
        return toBean(getPayloads(token), UserVO.class);
    }


    /**
     * 验证 token 且正确
     * @param token Auth 服务颁发的登录 Token（Login 接口获取）
     * @return Token 能否解析成功
     */
    @Override
    public Boolean verifyToken(String token) throws ReLoginException {
        if(isNotBlank(token)) {
            try {
                return JWTUtil.verify(token, key.getBytes());
            } catch (Exception e) {
                log.info("[TokenService::verifyToken] Verify Error!!! token={}; key={}", token, key);
                e.printStackTrace();
                throw new ReLoginException();
            }
        }
        throw new ReLoginException();
    }


    /**
     * JWT解析token返回用户信息
     * @param token token
     * @return 用户信息
     */
    public JSONObject getPayloads(String token) {
        return JWTUtil.parseToken(token).getPayloads();
    }
}
