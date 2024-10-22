package com.clinic.controller;

import com.bbs.Result;
import com.clinic.app.drug.order.AppDrugOrderService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DrugOrderController {

    private AppDrugOrderService service;

    @Data
    public static class AddDrugOrderParam {

        private String name;

        private String receiverAddress;

        private String orderDetail;

    }


    @PutMapping("/order")
    public Result<Long> add(AddDrugOrderParam param) {
        return Result.success(service.add(param));
    }






    @Autowired
    public DrugOrderController(AppDrugOrderService service) {
        this.service = service;
    }
}
