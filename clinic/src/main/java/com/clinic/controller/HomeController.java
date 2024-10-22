package com.clinic.controller;

import com.bbs.Result;
import com.clinic.cache.set.BusinessCache;
import com.clinic.dto.BusinessVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

@Slf4j
@RestController
public class HomeController {

    @Resource
    private BusinessCache busCache;

    /**
     * 查询营业实例
     */
    @GetMapping("/business")
    public Result<BusinessVO> getBusiness() {
        return Result.success(busCache.get());
    }

    /**
     * 开始/停止营业
     *
     * @param workFlag 工作标识符:0:营业;1:不营业;
     */
    @GetMapping("/reverse/business")
    public Result<Boolean> reverseBusiness(@RequestParam Integer workFlag) {
        return Result.success(Objects.nonNull(busCache.reverseWork(workFlag)) ? true : false);
    }
}