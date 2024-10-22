package com.bbs.auth.api.vx.pay;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Security;

@Component
@Data
public class WxPaySdkConfig implements InitializingBean {

    /** 微信支付域名 */
    @Value(value = "${wx.oa.appId}")
    public String appId;//"wxbd377f32afa2d442"

    /** 商户号 */
    @Value(value = "${wx.pay.merchantId}")
    public String merchantId;//"1683753833"

    /** 商户API私钥路径 */
    @Value(value = "${wx.pay.privateKeyPath}")
    public String privateKeyPath;//E:\WXCertUtil\cert\1681624897_20240723_cert\apiclient_cert.p12

    /** 商户证书序列号 */
    @Value(value = "${wx.pay.merchantSerialNumber}")
    public String merchantSerialNumber;//"30F598E21FFCC168EC548ED29386EC8573BD41A4"

    /** 商户APIV3密钥 */
    @Value(value = "${wx.pay.apiV3Key}")
    public String apiV3Key;//"sgdfhen274612BDGFSndcVBS6BD5r4sf"

    private Config wxMlConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        Security.setProperty("crypto.policy", "unlimited");
        wxMlConfig = new RSAAutoCertificateConfig.Builder()
                .merchantId(merchantId)
                // 使用 com.wechat.pay.java.core.util 中的函数从本地文件中加载商户私钥，商户私钥会用来生成请求的签名
                .privateKeyFromPath(privateKeyPath)
                .merchantSerialNumber(merchantSerialNumber)
                .apiV3Key(apiV3Key)
                .build();
    }

}
