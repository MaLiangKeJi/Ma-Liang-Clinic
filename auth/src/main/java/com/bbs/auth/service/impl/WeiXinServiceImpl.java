package com.bbs.auth.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.bbs.auth.cache.user.UserCache;
import com.bbs.auth.entity.User;
import com.bbs.auth.entity.UserCompany;
import com.bbs.auth.service.CompanyService;
import com.bbs.auth.service.TokenService;
import com.bbs.auth.service.UserService;
import com.bbs.auth.service.WeiXinService;
import com.bbs.auth.util.RedisUtil;
import com.bbs.auth.util.WxUtil;
import com.bbs.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;

@Service
@Slf4j
public class WeiXinServiceImpl implements WeiXinService {

    @Value("${wx.oa.token}")
    private String token;

    @Resource
    private UserService userService;  //用户

    @Resource
    private UserCache userCache;

    @Resource
    private TokenService tokenService;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private WxUtil wxUtil;
    @Resource
    private CompanyService companyService;



    @Override
    public Map<String,String> getQrCode() {
        log.info("getQrCode方法开始执行！");
        // 获取 AccessToken
        String accessToken;
        try {
            accessToken = wxUtil.getAccessToken();
            log.debug("获取到的acesstoken为：‘{}’",accessToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("获取AccessToken异常");
        }
        // 获取ticket
        String ticket;
        String expireSeconds;
        try {
            String url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accessToken;
            // 组织请求数据
            Map<String, Object> jsonData = new HashMap<>();
            jsonData.put("expire_seconds", 1800); // 二维码过期时间
            jsonData.put("action_name", "QR_SCENE");
            Map<String, Object> actionInfo = new HashMap<>();
            Map<String, Object> scene = new HashMap<>();
            scene.put("scene_str", "3D");
            actionInfo.put("scene", scene);
            jsonData.put("action_info", actionInfo);
            // 发送请求
            String result = HttpRequest.post(url).body(JSON.toJSONString(jsonData)).execute().body();
            log.debug("请求微信接口的结果:'{}'",result);
            // 结果处理
            com.alibaba.fastjson2.JSONObject ticketJson = com.alibaba.fastjson2.JSONObject.parseObject(result);
            ticket = ticketJson.getString("ticket");
            expireSeconds = ticketJson.getString("expire_seconds");

            redisUtil.set("WX:"+ticket, "1", Long.parseLong(expireSeconds));

            // 通过ticket换取二维码 https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=
            HashMap<String, String> map = new HashMap<>();
            map.put("ticket", ticket);
            map.put("expire_seconds", expireSeconds);
            log.info("getQrCode方法执行结束！");
            return map;
        } catch (HttpException e) {
            e.printStackTrace();
            throw new BusinessException("获取tikect异常");
        }
    }

    @Override
    public String receive(String signature, String timestamp, String nonce, String echostr, HttpServletRequest request) throws IOException {
        String xml = "success";
        log.info("微信回调方法开始执行");
        // 验证微信签名
        if (!wxUtil.checkSignature(signature, timestamp, nonce, token)){
            throw new BusinessException("微信回调参数异常！");
        }
        // 验证服务端配置
        if (echostr != null){
            return echostr;
        }
        // 接收微信推送的消息
        String xmlString = wxUtil.readRequest(request);
        try{
            Map<String, String> resXml = wxUtil.ResponseXmlToMap(xmlString);

            String fromUserName = resXml.get("FromUserName"); // 获取OpenId
            String toUserName = resXml.get("ToUserName");//开发者微信号
            String msgType = resXml.get("MsgType");//消息类型，event
            if (msgType.equals("event")) {
                String event = resXml.get("Event");
                String ticket = resXml.get("Ticket"); // 获取二维码凭证

                String user;
                switch (event){
                    case "subscribe": //扫描带参数二维码事件-未关注
                        if(StrUtil.isEmpty(ticket)){
                            return xml;
                        }
                        log.debug("处理“扫描带参数二维码事件-未关注”事件入参ticket：{}",ticket);
                        // 处理绑定微信号事件
                        user = redisUtil.get("WX:" + ticket);
                        if ("1".equals(user)){
                            //先删除
                            redisUtil.delete("WX:"+ticket);
                            redisUtil.set("WX:"+ticket, fromUserName,100000L);
                        }
                        xml ="<xml>\n" +
                                "  <ToUserName><![CDATA[" + fromUserName + "]]></ToUserName>\n" +
                                "  <FromUserName><![CDATA[" + toUserName + "]]></FromUserName>\n" +
                                "  <CreateTime> "+ System.currentTimeMillis() + "</CreateTime>\n" +
                                "  <MsgType><![CDATA[text]]></MsgType>\n" +
                                "  <Content><![CDATA[科技改变生活！关注成功]]></Content>\n" +
                                "</xml>>\n";
                        break;
                    case "unsubscribe": //取消关注
                        break;
                    case "SCAN":// 扫描带参数二维码事件-已关注
                        if(StrUtil.isEmpty(ticket)){
                            return xml;
                        }
                        log.debug("处理“扫描带参数二维码事件-已关注”事件入参ticket：{}",ticket);
                        // 处理绑定微信号事件
                        user = redisUtil.get("WX:" + ticket);
                        if ("1".equals(user)){
                            //先删除
                            redisUtil.delete("WX:"+ticket);
                            redisUtil.set("WX:"+ticket, fromUserName,100000L);
                        }
                        xml ="<xml>\n" +
                                "  <ToUserName><![CDATA[" + fromUserName + "]]></ToUserName>\n" +
                                "  <FromUserName><![CDATA[" + toUserName + "]]></FromUserName>\n" +
                                "  <CreateTime>" + System.currentTimeMillis() + "</CreateTime>\n" +
                                "  <MsgType><![CDATA[text]]></MsgType>\n" +
                                "  <Content><![CDATA[扫码成功!]]></Content>\n" +
                                "</xml>\n";
                        break;
                    case "CLICK":   //自定义菜单事件
                        String eventKey = resXml.get("EventKey");
                        switch(eventKey) {
                            case "url":
                                xml ="<xml>\n" +
                                        "  <ToUserName><![CDATA[" + fromUserName + "]]></ToUserName>\n" +
                                        "  <FromUserName><![CDATA[" + toUserName + "]]></FromUserName>\n" +
                                        "  <CreateTime>" + System.currentTimeMillis() + "</CreateTime>\n" +
                                        "  <MsgType><![CDATA[text]]></MsgType>\n" +
                                        "  <Content><![CDATA[请在电脑上打开此链接：https://maliang.work]]></Content>\n" +
                                        "</xml>\n";
                                break;
                            case "QrCode":
                                xml ="<xml>\n" +
                                        "  <ToUserName><![CDATA[" + fromUserName + "]]></ToUserName>\n" +
                                        "  <FromUserName><![CDATA[" + toUserName + "]]></FromUserName>\n" +
                                        "  <CreateTime>" + System.currentTimeMillis() + "</CreateTime>\n" +
                                        "   <MsgType><![CDATA[image]]></MsgType>\n" +
                                        "  <Image>\n" +
                                        "  <MediaId><![CDATA[-gIBIvvHZ0-oip6xzJA_I768rudC9fZwsAibkGpFlrpZdh1sm0t5EWrfxjnlOcrn]]></MediaId>\n" +
                                        "  </Image>\n" +
                                        "</xml>\n";
                                break;
                            case "admin_url"    :
                                xml ="<xml>\n" +
                                        "  <ToUserName><![CDATA[" + fromUserName + "]]></ToUserName>\n" +
                                        "  <FromUserName><![CDATA[" + toUserName + "]]></FromUserName>\n" +
                                        "  <CreateTime>" + System.currentTimeMillis() + "</CreateTime>\n" +
                                        "  <MsgType><![CDATA[text]]></MsgType>\n" +
                                        "  <Content><![CDATA[请在电脑上打开此链接：https://maliang.work/clinic/back/invite/me]]></Content>\n" +
                                        "</xml>\n";
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }else if (msgType.equals("text")) {
                String Content = resXml.get("Content");//文本消息内容
                String MsgId = resXml.get("MsgId");//消息id，64位整型
                log.debug(Content);
            }
        }catch (Exception e){
            log.error("微信回调方法执行异常！{}",e.getMessage());
            e.printStackTrace();
            throw new BusinessException("系统异常");
        }
        log.info("微信回调方法执行结束！");
        return xml;
    }


    @Override
    public Map<String, Object> checkLogin(String ticket, Integer expireNumber){
        log.info("checkLogin方法开始执行，入参ticket：{}",ticket);
        // 从缓存获取扫码状态
        String openId;
        try {
            openId = redisUtil.get("WX:"+ticket);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.debug("redis中的openid：{}，1表达没有扫码或没有回调或没有关注关注号",openId);
        // 判断扫码状态
        if (StrUtil.isEmpty(ticket)){
            throw new BusinessException("WX:"+ticket+"的值为空");
        }
        if (openId == null){
            //说明二维码过期了，停止轮询
            HashMap<String, Object> scanResultMap2 = new HashMap<>();
            scanResultMap2.put("scanResult",-2);
            return scanResultMap2;
        }
        if(openId.equals("1")){
            //1表达没有回调，没有关注关注号
            HashMap<String, Object> scanResultMap = new HashMap<>();
            scanResultMap.put("scanResult",-1);
            return scanResultMap;
        }
        User dbUser = userService.searchIdByOpenId(openId);
        // 判断用户是否存在
        if (isNull(dbUser) || isNull(dbUser.getPhone())){
            HashMap<String, Object> scanResultMap3 = new HashMap<>();
            SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, token.getBytes());
            scanResultMap3.put("openId", aes.encrypt(openId));
            scanResultMap3.put("scanResult",0);
            return scanResultMap3;
        }
        //生成token
        String token = tokenService.createToken(dbUser);
        HashMap<String, Object> resultMap = new HashMap<>();

        tokenService.setLoginFlag(dbUser.getId(), expireNumber, TimeUnit.DAYS);
        userCache.expireUserAndPhoneMap(dbUser);

        List<UserCompany> userCompanyList = companyService.searchCompany(dbUser.getId());

        resultMap.put("token", token);
        resultMap.put("user", dbUser);
        resultMap.put("scanResult",1);
        resultMap.put("userCompanyList", userCompanyList);
        //公众号下发登录成功
        wxUtil.sendLoginMassage(openId, dbUser);
        log.info("checkLogin方法执行结束！");
        return resultMap;
    }

    @Override
    public String createMenu() {
        // 获取 AccessToken
        String accessToken;
        try {
            accessToken = wxUtil.getAccessToken();
            log.debug("获取到的acesstoken为：‘{}’",accessToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("获取AccessToken异常");
        }
        String url="https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessToken;
        JSONObject big = new JSONObject();
        JSONArray button = new JSONArray();

        JSONObject one = new JSONObject();

        JSONArray sub_button = new JSONArray();

        JSONObject subone = new JSONObject();
        subone.put("name","系统链接");
        subone.put("type","click");
        subone.put("key","admin_url");
        JSONObject subone1 = new JSONObject();
        subone1.put("name","联系我们");
        subone1.put("type","click");
        subone1.put("key","QrCode");

        sub_button.put(subone);
        sub_button.put(subone1);

        one.put("name","我是药企");
        one.put("type","click");
        one.put("sub_button",sub_button);

        JSONObject two = new JSONObject();

        JSONArray twoSub = new JSONArray();
        JSONObject subTwo1 = new JSONObject();
        subTwo1.put("name","找诊所");
        subTwo1.put("type","view");
        subTwo1.put("url",wxUtil.oAuth("https://maliang.work/clinic/phone/patient/clinic"));
        JSONObject subTwo2 = new JSONObject();
        subTwo2.put("name","个人中心");
        subTwo2.put("type","view");
        subTwo2.put("url",wxUtil.oAuth("https://maliang.work/clinic/phone/patient/index"));
        JSONObject subTwo3 = new JSONObject();
        subTwo3.put("name","论坛");
        subTwo3.put("type","view");
        subTwo3.put("url","https://forum.maliang.work/");

        twoSub.put(subTwo1);
        twoSub.put(subTwo2);
        twoSub.put(subTwo3);

        two.put("name","我是患者");
        two.put("type","click");
        two.put("sub_button",twoSub);

        JSONObject three = new JSONObject();
        JSONArray threeSub = new JSONArray();

        JSONObject subThree1 = new JSONObject();
        subThree1.put("name","诊所入驻");
        subThree1.put("type","click");
        subThree1.put("key","url");
        JSONObject subThree2 = new JSONObject();
        subThree2.put("name","患者查询");
        subThree2.put("type","view");
        subThree2.put("url",wxUtil.oAuth("https://maliang.work/clinic/phone/doctor/patient"));
        JSONObject subThree3 = new JSONObject();
        subThree3.put("name","留言回复");
        subThree3.put("type","view");
        subThree3.put("url",wxUtil.oAuth("https://maliang.work/clinic/phone/doctor/message"));
        JSONObject subThree4 = new JSONObject();
        subThree4.put("name","查看预约");
        subThree4.put("type","view");
        subThree4.put("url",wxUtil.oAuth("https://maliang.work/clinic/phone/doctor/index"));

        threeSub.put(subThree1);
        threeSub.put(subThree2);
        threeSub.put(subThree3);
        threeSub.put(subThree4);

        three.put("name","我是医生");
        three.put("type","click");
        three.put("sub_button",threeSub);

        button.put(one);
        button.put(two);
        button.put(three);
        big.put("button",button);

        return HttpRequest.post(url).body(JSON.toJSONString(big)).execute().body();
    }

}