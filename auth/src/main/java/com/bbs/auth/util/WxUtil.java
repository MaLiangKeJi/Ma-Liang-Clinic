package com.bbs.auth.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.bbs.auth.entity.User;
import com.bbs.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Slf4j
@Component
public class WxUtil {

    @Value("${wx.oa.appId}")
    private String appId;
    @Value("${wx.oa.appSecret}")
    private String appSecret;
    @Resource
    private RedisUtil redisUtil;

    /**
     * 用于获取 AccessToken (微信接口调用凭证)
     * 由于微信限制每日只能获取 2000 次 AccessToken,使用缓存进行存储,避免接口调用次数过多
     * @return AccessToken
     */
    public String getAccessToken(){
        // 查询Redis,若存在则直接返回
        String accessToken = redisUtil.get("WEI_XIN_ACCESS_TOKEN");
        if (accessToken != null){
            return accessToken;
        }
        // 缓存不存在,向微信请求AccessToken
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        params.put("secret", appSecret);
        params.put("grant_type", "client_credential");
        // 发送GET请求
        accessToken = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token?", params);
        // 处理结果
        JSONObject jsonObject = JSONObject.parseObject(accessToken);
        System.out.println(jsonObject);
        accessToken = jsonObject.getString("access_token");
        // 存入缓存
        if (StrUtil.isNotEmpty(accessToken) && accessToken != null){
            redisUtil.set("WEI_XIN_ACCESS_TOKEN",accessToken,60*60*2L);
        }else {
            log.info("微信返回的accessToken为空");
            throw new BusinessException("微信返回的accessToken为空");
        }
        return accessToken;
    }


    /**
     * 给网页加授权域名
     *
     * @return
     */
    public String oAuth(String redircet_uri) {
        try {
            redircet_uri = URLEncoder.encode(redircet_uri, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri=" + redircet_uri + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    }


    /**
     * 读取 Request Body 内容作为字符串
     * @param request HttpServletRequest
     * @return XmlString
     * @throws IOException XmlIO
     */
    public String readRequest(HttpServletRequest request) throws IOException {
        StringBuffer sb = new StringBuffer();
        InputStream inputStream = request.getInputStream();
        String str;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((str = in.readLine()) != null) {
            sb.append(str);
        }
        in.close();
        inputStream.close();
        return sb.toString();
    }

    /**
     * 将微信获取的XML结果转为Map
     * @param xmlString xml
     * @return Map
     * @throws DocumentException DocumentException
     */
    public Map<String, String> ResponseXmlToMap(String xmlString) throws DocumentException {
        // 解析 XML 字符串为 Document 对象
        Document document = DocumentHelper.parseText(xmlString);
        // 获取根元素
        Element rootElement = document.getRootElement();
        // 获取子元素
        List<Element> nodes = rootElement.elements();
        // 获取子元素的文本内容
        Map<String, String> resultMap = new HashMap<>();
        for (Node node:nodes ) {
            Element element = (Element) node;
            String nodeName = element.getName();
            String nodeText = element.getTextTrim();
            resultMap.put(nodeName, nodeText);
        }
        if (CollUtil.isEmpty(resultMap) && resultMap.containsKey("errcode")) {
            throw new BusinessException("系统异常");
        }
        return resultMap;
    }


    /**
     * 验证微信签名,确保接收到的消息来自微信官方
     */
    public boolean checkSignature(String signature, String timestamp,String nonce, String token) {
        // 1.将token、timestamp、nonce三个参数进行字典序排序
        String[] arr = new String[]{token, timestamp, nonce};
        Arrays.sort(arr);
        // 2. 将三个参数字符串拼接成一个字符串进行sha1加密
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        content = null;
        // 3.将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return tmpStr != null && tmpStr.equalsIgnoreCase(signature.toUpperCase());
    }

    private String byteToStr(byte[] byteArray) {
        StringBuilder strDigest = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            strDigest.append(byteToHexStr(byteArray[i]));
        }
        return strDigest.toString();
    }

    private String byteToHexStr(byte mByte) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = Digit[mByte & 0X0F];
        String s = new String(tempArr);
        return s;
    }


    /**
     * 发送注册消息
     */
    public void sendRegisterMassage(String openId, User dbUser){
        log.debug("注册成功![Login::login] user={}", JSONUtil.toJsonPrettyStr(dbUser));
        // 获取 AccessToken
        String accessToken = getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
        // 组织请求数据
        Map<String, Object> data = new HashMap<>();

        // 注册姓名
        Map<String, String> keyword3 = new HashMap<>();
        keyword3.put("value", dbUser.getPhone()+"");
        data.put("thing2",keyword3);
        // 注册手机号
        Map<String, String> keyword2 = new HashMap<>();
        keyword2.put("value", dbUser.getPhone()+"");
        data.put("phone_number1",keyword2);
        // 注册平台
        Map<String, String> keyword1 = new HashMap<>();
        keyword1.put("value", "诊所系统");
        data.put("thing4",keyword1);
        // 注册日期
        Map<String, String> keyword4 = new HashMap<>();
        keyword4.put("value", new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
        data.put("time5",keyword4);

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("touser", openId);
        jsonData.put("template_id", "fJFF_ZdFeD-35UQnlWbcTiCVbPCfzmwjK28hGKlf4zU");
//        jsonData.put("client_msg_id", openId); // 防重入id（对于同一个openid + client_msg_id, 10分钟内只发送一条消息）
        jsonData.put("data", data);
        // 发送请求
        String result = HttpRequest.post(url).body(JSON.toJSONString(jsonData)).execute().body();
        // 结果处理
        JSONObject ticketJson = JSONObject.parseObject(result);
        Integer errcode = ticketJson.getInteger("errcode");
        if (errcode != 0){
            log.error(errcode + ":" + ticketJson.getString("errmsg"));
            throw new BusinessException("消息发送失败！");
        }
    }

    /**
     * 发送绑定消息
     */
    public void sendBindingMassage(String openId, User dbUser){
        log.debug("绑定成功![Login::login] user={}", JSONUtil.toJsonPrettyStr(dbUser));
        // 获取 AccessToken
        String accessToken = getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
        // 组织请求数据
        Map<String, Object> data = new HashMap<>();

        // 姓名
        Map<String, String> keyword3 = new HashMap<>();
        keyword3.put("value", dbUser.getName());
        data.put("thing2",keyword3);
        // 绑定时间
        Map<String, String> keyword4 = new HashMap<>();
        keyword4.put("value", new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
        data.put("time4",keyword4);
//        // 软件名称
//        Map<String, String> keyword1 = new HashMap<>();
//        keyword1.put("value", "诊所系统");
//        data.put("thing8",keyword1);
        // 手机号
        Map<String, String> keyword2 = new HashMap<>();
        keyword2.put("value", dbUser.getPhone()+"");
        data.put("phone_number3",keyword2);

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("touser", openId);
        jsonData.put("template_id", "adLInV-JT06goQSynlfrUBZl7MDLyHUrkrnVPd0F4Uw");
//        jsonData.put("client_msg_id", openId); // 防重入id（对于同一个openid + client_msg_id, 10分钟内只发送一条消息）
        jsonData.put("data", data);
        // 发送请求
        String result = HttpRequest.post(url).body(JSON.toJSONString(jsonData)).execute().body();
        // 结果处理
        JSONObject ticketJson = JSONObject.parseObject(result);
        Integer errcode = ticketJson.getInteger("errcode");
        if (errcode != 0){
            log.error(errcode + ":" + ticketJson.getString("errmsg"));
            throw new BusinessException("消息发送失败！");
        }
    }

    /**
     * 发送登录消息
     */
    public void sendLoginMassage(String openId, User dbUser){
        log.debug("登录成功![Login::login] user={}", JSONUtil.toJsonPrettyStr(dbUser));
        // 获取 AccessToken
        String accessToken = getAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + accessToken;
        // 组织请求数据
        Map<String, Object> data = new HashMap<>();

        // 用户账号
        Map<String, String> keyword3 = new HashMap<>();
        keyword3.put("value", dbUser.getPhone()+"");
        data.put("character_string6",keyword3);
        // 登录时间
        Map<String, String> keyword4 = new HashMap<>();
        keyword4.put("value", new SimpleDateFormat("yyyy年MM月dd日 HH:mm").format(new Date()));
        data.put("time1",keyword4);
//        // 软件名称
//        Map<String, String> keyword1 = new HashMap<>();
//        keyword1.put("value", "诊所系统");
//        data.put("thing8",keyword1);
//        // 登录网址
//        Map<String, String> keyword2 = new HashMap<>();
//        keyword2.put("value", "www.123.com");
//        data.put("thing5",keyword2);

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("touser", openId);
        jsonData.put("template_id", "30_oq_2GlEMfPkUunUk2HzXdbwF04aO1hjwMwymxA5Q");
//        jsonData.put("client_msg_id", openId); // 防重入id（对于同一个openid + client_msg_id, 10分钟内只发送一条消息）
        jsonData.put("data", data);
        // 发送请求
        String result = HttpRequest.post(url).body(JSON.toJSONString(jsonData)).execute().body();
        // 结果处理
        JSONObject ticketJson = JSONObject.parseObject(result);
        Integer errcode = ticketJson.getInteger("errcode");
        if (errcode != 0){
            log.error(errcode + ":" + ticketJson.getString("errmsg"));
            throw new BusinessException("消息发送失败！");
        }
    }


}
