package com.clinic.controller;

import com.bbs.Result;
import com.clinic.app.AppStockService;
import com.clinic.dto.param.QueryStockInParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/stock/in")
public class StockInController {

    private AppStockService app;





    /**
     * 药品入库（批量）
     */
    @GetMapping()
    public Result query(QueryStockInParam param) {
        return app.queryStockIn(param);
    }






    @Resource
    public void setAppStockService(AppStockService appStockService) {
        this.app = appStockService;
    }
}
