package com.bbs.auth.util.captcha;

import com.alibaba.fastjson.JSON;
import com.bbs.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

/**
 * 飞鸽云短信服务
 * @see <a href=https://next.api.aliyun.com/api/Dysmsapi/2017-05-25/SendSms?params={%22SignName%22:%22%E6%B5%8E%E6%BA%90%E5%95%86%E4%B8%9A%E5%B9%B3%E5%8F%B0%22,%22TemplateCode%22:%22SMS_465343610%22,%22PhoneNumbers%22:%2217634860778%22,%22TemplateParam%22:%22%7B%5C%22code%5C%22%3A%5C%221234%5C%22%7D%22}&tab=DOC>阿里云短信文档</a>
 */
@Slf4j
@RestController
@RequestMapping
public class FeiGeYunCaptcha{// extends CaptchaUtil

    @Value("${code.feigeyun.apikey}")
    private String apikey;

    @Value("${code.feigeyun.secret}")
    private String accessKeySecret;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        public static final String OK = "OK";

        /**
         *  接口回执状态码，判断成功失败的标志  成功为 0，其它请参考 API 错误代码
         */
        private String code;

        /**
         * 接口回执状态描述  成功为 OK，其它请参考 API 错误代码
         */
        private String msg;

        /**
         * 短信回执编号，为唯一识别码  默认格式如：2016072909264497197473179
         */
        private String msg_no;

        /**
         * 计费数量
         */
        private String count;
    }

//    @Override
    public void send(String phoneNumber, String signName, String templateCode, String templateParam) throws Exception {
        log.debug("飞鸽云短信-{}发送短信: signName={}; templateCode={}", phoneNumber, signName, templateParam);

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("https://api.4321.sh/sms/template");
        httpPost.addHeader("Content-Type","application/json");
        Map<String,Object> map = new HashMap<>();
        map.put("apikey",apikey);//账号
        map.put("secret",accessKeySecret);//密钥
        map.put("mobile",phoneNumber);//手机号
        map.put("sign_id",signName);//签名id
        map.put("template_id",templateCode);//模板id
        map.put("content",templateParam);//短信内容
        String json = JSON.toJSONString(map);
        log.debug("飞鸽云短信-{}请求数据: {}", phoneNumber, json);
        httpPost.setEntity(new StringEntity(json,"UTF-8"));
        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String res = EntityUtils.toString(entity);
        log.debug("飞鸽云短信-{}响应数据: {}", phoneNumber, res);
        Response respObj = JSON.parseObject(res, Response.class);
        if(isNull(respObj) || !Response.OK.equals(respObj.msg)) {
            log.error(
                    "飞鸽云短信-{}异常: 发送短信失败！！！需要联系工程师处理 config: accessKey={}; accessKeySecret={};",
                    phoneNumber,
                    apikey,
                    accessKeySecret
            );
            throw new BusinessException();
        }
    }
}
