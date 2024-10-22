package com.bbs.auth.util.captcha;

import cn.hutool.json.JSONUtil;
import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.bbs.exception.BusinessException;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static java.util.Objects.isNull;

/**
 * 阿里云短信服务
 * @see <a href=https://next.api.aliyun.com/api/Dysmsapi/2017-05-25/SendSms?params={%22SignName%22:%22%E6%B5%8E%E6%BA%90%E5%95%86%E4%B8%9A%E5%B9%B3%E5%8F%B0%22,%22TemplateCode%22:%22SMS_465343610%22,%22PhoneNumbers%22:%2217634860778%22,%22TemplateParam%22:%22%7B%5C%22code%5C%22%3A%5C%221234%5C%22%7D%22}&tab=DOC>阿里云短信文档</a>
 */
@Slf4j
@RestController
@RequestMapping
public class ALiYunCaptcha  extends CaptchaUtil {//extends CaptchaUtil

    private static final String REGION = "cn-hangzhou";

    @Value("${code.alibaba.access.key}")
    private String accessKey;

    @Value("${code.alibaba.access.secret}")
    private String accessKeySecret;

    @Value("${code.alibaba.endpoint}")
    private String endpoint;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {

        public static final String OK = "OK";

        /**
         * 请求状态码（例：OK）
         * @see <a href=https://help.aliyun.com/zh/sms/developer-reference/api-error-codes?spm=api-workbench.api_explorer.0.0.3e8c30c3oi37Jm>状态码 API</a>
         */
        private String code;

        /**
         * 状态码的描述
         */
        private String message;

        /**
         * 发送回执 ID
         */
        private String bizId;

        /**
         * 请求 ID
         */
        private String requestId;
    }

    @Override
    public void send(String phoneNumber, String signName, String templateCode, String templateParam) throws Exception {
        log.debug("阿里云短信-{}发送短信: signName={}; templateCode={}; templateParam={}", phoneNumber, signName, templateCode, templateParam);
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(accessKey)
                .accessKeySecret(accessKeySecret)
                .build());

        AsyncClient client = AsyncClient.builder()
                .region(REGION) // Region ID
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                .setEndpointOverride(endpoint)
                )
                .build();
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .signName(signName)
                .templateCode(templateCode)
                .phoneNumbers(phoneNumber)
                .templateParam(templateParam)
                .build();

        CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
        SendSmsResponse resp = response.get();
        client.close();
        log.debug("阿里云短信-{}响应数据: {}", phoneNumber, new Gson().toJson(resp));
        Response respObj = JSONUtil.toBean(new Gson().toJson(resp.getBody()), Response.class);
        if(isNull(respObj) || !Response.OK.equals(respObj.code)) {
            log.error(
                    "阿里云短信-{}异常: 发送短信失败！！！需要联系工程师处理 config: accessKey={}; accessKeySecret={}; endpoint={};",
                    phoneNumber,
                    accessKey,
                    accessKeySecret,
                    endpoint
            );
            throw new BusinessException();
        }
    }
}
