package com.bbs.auth.api.vx;

import com.bbs.Result;
import com.bbs.auth.service.WeiXinService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@Api(tags = "微信登录相关接口")
@RestController
@Slf4j
public class WXLoginAuthAPI {

    @Resource
    private WeiXinService weiXinService;


    @GetMapping(value = "/weixin/receive")
    @ApiOperation("接收微信消息事件,判断用户是否完成扫码关注")
    public String getWxLoginReceive(HttpServletRequest request) throws IOException {
        log.debug("微信回调接口开始执行get请求/weixin/receive");
        // 获取微信请求参数
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        log.debug("开始校验此次消息是否来自微信服务器，param->signature:{},\ntimestamp:{},\nnonce:{},\nechostr:{}",
                signature, timestamp, nonce, echostr);
        String result = weiXinService.receive(signature, timestamp, nonce, echostr, request);
        log.debug("微信回调接口get请求执行结束！");
        return result;
    }


    @PostMapping(value = "/weixin/receive")
    @ApiOperation("接收微信消息事件,判断用户是否完成扫码关注")
    public String postWxLoginReceive(HttpServletRequest request) throws IOException {
        log.debug("微信回调接口开始执行post请求/weixin/receive");
        // 获取微信请求参数
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        log.debug("开始校验此次消息是否来自微信服务器，param->signature:{},\ntimestamp:{},\nnonce:{},\nechostr:{}",
                signature, timestamp, nonce, echostr);
        String result = weiXinService.receive(signature,timestamp,nonce,echostr,request);
        log.debug("微信回调接口post请求执行结束！");
        return result;
    }


    @ApiOperation("微信扫码登录，提供二维码")
    @PostMapping(value = "/weixin/getQRCode")
    public Result<Map<String, String>> weinLogin(){
        log.debug("微信扫码登录接口开始执行：/weixin/getQRCode");
        //获取ticket
        Map<String, String> codeResult = weiXinService.getQrCode();
        log.debug("微信扫码登录接口执行结束！");
        return Result.success(codeResult);
    }

    @GetMapping("/weixin/check")
    @ApiOperation("获取扫码登录状态,前端进行轮询")
    public Result<Map<String, Object>> checkLogin(@RequestParam String ticket, @RequestParam(required = false) Integer expireNumber) {
        log.debug("前端二维码轮询接口开始执行/weixin/check");
        try {
            Map<String, Object> resultMap = weiXinService.checkLogin(ticket, expireNumber);
            log.debug("前端二维码轮询接口执行结束！");
            return Result.success(resultMap);
        }catch (Exception e){
            log.error("前端二维码轮询接口执行失败！{}",e.getMessage());
            return Result.failed(e.getMessage());
        }
    }
}
