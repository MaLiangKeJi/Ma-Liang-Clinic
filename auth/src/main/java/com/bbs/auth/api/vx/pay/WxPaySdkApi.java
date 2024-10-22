package com.bbs.auth.api.vx.pay;

import com.bbs.Result;
import com.bbs.auth.entity.RenewLog;
import com.bbs.auth.service.RenewLogService;
import com.bbs.auth.util.LoginUser;
import com.wechat.pay.java.core.exception.HttpException;
import com.wechat.pay.java.core.exception.MalformedMessageException;
import com.wechat.pay.java.core.exception.ServiceException;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.CloseOrderRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import com.wechat.pay.java.service.payments.nativepay.model.QueryOrderByIdRequest;
import com.wechat.pay.java.service.payments.nativepay.model.QueryOrderByOutTradeNoRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class WxPaySdkApi {

    private NativePayService service;

    @Resource
    private WxPaySdkConfig config;
    @Resource
    private RenewLogService renewLogService;


    @GetMapping("/weixin/clinic/pay/qrcode")
    public Result<Map<String, Object>> getPayCode(Integer packageType) {
        log.info("获取支付二维码");
        // 初始化服务
        service = new NativePayService.Builder().config(config.getWxMlConfig()).build();
        try {
            String orderId = "tradeNo"+System.currentTimeMillis()/1000+config.merchantId;
            Order order = prepay(orderId,packageType);
            PrepayResponse prepay = order.getPrepayResponse();
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("orderId", orderId);
            resultMap.put("codeUrl", prepay.getCodeUrl());
            // 保存订单
            RenewLog renewLog = new RenewLog();
            renewLog.setBackSystemId(3L);
            renewLog.setCreateBy(LoginUser.getId());
            renewLog.setOrderId(orderId);
            renewLog.setStatus(0);
            renewLog.setPackageType(packageType);
            renewLog.setMoney(order.getMoney());
            renewLogService.save(renewLog);

            return Result.success(resultMap);
        } catch (HttpException e) { // 发送HTTP请求失败
            log.error("请求失败:{}", e.getHttpRequest());
            // 调用e.getHttpRequest()获取请求打印日志或上报监控，更多方法见HttpException定义
        } catch (ServiceException e) { // 服务返回状态小于200或大于等于300，例如500
            log.error("服务失败:{}", e.getResponseBody());
            // 调用e.getResponseBody()获取返回体打印日志或上报监控，更多方法见ServiceException定义
        } catch (MalformedMessageException e) { // 服务返回成功，返回体类型不合法，或者解析返回体失败
            log.error("解析失败:{}", e.getMessage());
            // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见MalformedMessageException定义
        }
        return Result.failed("支付失败");
    }

    @GetMapping("/weixin/clinic/pay/WxOrder")
    public Result<RenewLog> getByWxOrder(String orderId) {
        // 初始化服务
        service = new NativePayService.Builder().config(config.getWxMlConfig()).build();
        try {
            Transaction transaction = queryOrderByOutTradeNo(orderId);
            RenewLog renewLog = renewLogService.getByOrderId(transaction.getOutTradeNo());
            if (transaction.getTradeState() == Transaction.TradeStateEnum.SUCCESS) {
                // 订单已支付
                if(renewLog.getStatus()==0){//未支付
                    renewLog.setCreateBy(LoginUser.getId());
                    renewLog.setStatus(1);
                    renewLogService.updateById(renewLog);
                }
            } else if (transaction.getTradeState() == Transaction.TradeStateEnum.CLOSED) {
                // 订单已关闭
                renewLog.setDeleteFlag(1);
                renewLogService.updateById(renewLog);
            } else if (transaction.getTradeState() == Transaction.TradeStateEnum.REFUND) {
                // 订单已退款
                renewLog.setStatus(2);
                renewLogService.updateById(renewLog);
            } else if (transaction.getTradeState() == Transaction.TradeStateEnum.NOTPAY) {
            }
            return Result.success(renewLog);
        } catch (HttpException e) { // 发送HTTP请求失败
            log.error("请求失败:{}", e.getHttpRequest());
            // 调用e.getHttpRequest()获取请求打印日志或上报监控，更多方法见HttpException定义
        } catch (ServiceException e) { // 服务返回状态小于200或大于等于300，例如500
            log.error("服务失败:{}", e.getResponseBody());
            // 调用e.getResponseBody()获取返回体打印日志或上报监控，更多方法见ServiceException定义
        } catch (MalformedMessageException e) { // 服务返回成功，返回体类型不合法，或者解析返回体失败
            log.error("解析失败:{}", e.getMessage());
            // 调用e.getMessage()获取信息打印日志或上报监控，更多方法见MalformedMessageException定义
        }
        return Result.failed("支付失败");
    }

    /** 如果支付退款，需要关闭订单 */
    public void closeOrder() {
        CloseOrderRequest request = new CloseOrderRequest();
        // 调用request.setXxx(val)设置所需参数，具体参数可见Request定义
        // 调用接口
        service.closeOrder(request);
    }

    @Data
    @AllArgsConstructor
    private static class Order {

        private PrepayResponse prepayResponse;

        private BigDecimal money;
    }

    /**
     * Native支付预下单
     */
    public Order prepay(String outTradeNo, Integer packageType) {
        int total = 0;
        switch (packageType){
            case 1:
                total = 4000;
                break;
            case 2:
                total = 11000;
                break;
            case 3:
                total = 36500;
                break;
        }
        PrepayRequest request = new PrepayRequest();
        request.setAppid(config.appId);
        request.setMchid(config.merchantId);
        Amount amount = new Amount();
        amount.setTotal(total);
        request.setAmount(amount);
        request.setDescription("码良科技-支付-诊所系统");
        request.setAttach("码良科技-支付-诊所系统");
        request.setNotifyUrl("https://www.maliang.work/api/auth/weixin/clinic/pay/notification");//回调地址
        request.setOutTradeNo(outTradeNo);//商户订单号
        // 调用接口
        return new Order(service.prepay(request), new BigDecimal(total * 0.01));
    }

    /** 微信支付订单号查询订单 */
    public Transaction queryOrderById() {
        QueryOrderByIdRequest request = new QueryOrderByIdRequest();
        request.setTransactionId("transaction_id_001");
        request.setMchid(config.merchantId);

        return service.queryOrderById(request);
    }

    /** 商户订单号查询订单 */
    public Transaction queryOrderByOutTradeNo(String orderId) {
        QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
        request.setOutTradeNo(orderId);
        request.setMchid(config.merchantId);
        return service.queryOrderByOutTradeNo(request);
    }



}
