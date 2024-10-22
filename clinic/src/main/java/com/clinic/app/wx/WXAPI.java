package com.clinic.app.wx;

import com.bbs.Result;
import com.clinic.service.WeiXinLoginService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@Slf4j
public class WXAPI {

    @Resource
    private WeiXinLoginService weiXinLoginService;


    @GetMapping("/weixin/checkPatient")
    @ApiOperation("验证当前手机号的病人是否扫码关注")
    public Result<Map<String, Object>> checkPhone(HttpServletRequest req, @RequestParam String ticket, @RequestParam Long phone, @RequestParam boolean isEnd, @RequestParam Long timestamp) {
        try {
            log.debug("前端二维码轮询接口开始执行/weixin/checkPatient,timestamp{}",timestamp);
            Map<String, Object> resultMap = weiXinLoginService.checkPhone(getWebsite(req), ticket, phone, isEnd);
            log.debug("前端二维码轮询接口执行结束！");
            return Result.success(resultMap);
        }catch (Exception e){
            return Result.failed(e.getMessage());
        }
    }

    /**
     * 获取官网网址
     */
    private String getWebsite(HttpServletRequest req) {
        String url = req.getRequestURL().toString();
        String uri = req.getRequestURI().toString();
        int websiteRight = url.indexOf(uri);
        return url.substring(0, websiteRight);
    }




}
