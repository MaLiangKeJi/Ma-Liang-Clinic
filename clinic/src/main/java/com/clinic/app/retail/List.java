package com.clinic.app.retail;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.bbs.util.PageUtil;
import com.clinic.entity.RetailRecord;
import com.clinic.service.RetailRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping
public class List {

    private final RetailRecordService drugRecordService;

    @GetMapping("/retail/list")
    public Result<Page<RetailRecord>> list(
            @RequestParam(required = false, defaultValue = "1") Integer current,
            @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestParam(required = false) String val,
            @RequestParam(required = false) Long startDate,
            @RequestParam(required = false) Long endDate
    ) {
        return Result.success(PageUtil.paginateWithInfo(drugRecordService.list(val, startDate, endDate), current, size));
    }

    @Autowired
    public List(RetailRecordService drugRecordService) {
        this.drugRecordService = drugRecordService;
    }
}
