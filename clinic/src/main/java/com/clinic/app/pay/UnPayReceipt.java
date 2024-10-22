package com.clinic.app.pay;

import com.bbs.Result;
import com.clinic.entity.Pay;
import com.clinic.service.PayService;
import com.clinic.util.LoginUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping
public class UnPayReceipt {

    @Resource
    private PayService payService;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Param {

        private Long id;
    }

    /**
     * 对未付款账单进行收款
     */
    @PostMapping("/pay/receipt")
    public Result<List<Pay>> payReceipt(@RequestBody Param param) {
        payService.lambdaUpdate()
                .eq(Pay::getCreator, LoginUser.getId())
                .eq(Pay::getId, param.getId())
                .set(Pay::getState, 1)
                .update();
        return Result.success(payService.searchUnPays());
    }
}
