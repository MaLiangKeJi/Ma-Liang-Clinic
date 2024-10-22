package com.clinic.app.count;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.clinic.app.log.admission.SearchList;
import com.clinic.controller.PayController;
import com.clinic.dto.PayAndRecordPageDto;
import com.clinic.entity.AdmissionLog;
import com.clinic.entity.Pay;
import com.clinic.service.PayService;
import com.clinic.util.LoginUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping
public class CountTow {

    @Resource
    private PayController payController;
    @Resource
    private SearchList searchList;
    @Resource
    private PayService payService;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Param {

        private SearchList.Param admissionLogParam;

        private SearchList.Param currentDayAdmissionLogParam;

        private PayController.AllIsPayParam currentDayProceedParam;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class VO {


        private Page<AdmissionLog> admissionLog;

        private Page<AdmissionLog> currentDayAdmissionLog;

        private Page<PayAndRecordPageDto> currentDayProceeds;

        private BigDecimal countFree;
    }

    @PostMapping("/count/tow")
    public Result<VO> count(@RequestBody Param param) {
        Page<AdmissionLog> admissionLog = searchList.search(param.admissionLogParam).getData();
        Page<AdmissionLog> currentDayAdmissionLog = searchList.search(param.currentDayAdmissionLogParam).getData();
        Page<PayAndRecordPageDto> currentDayProceeds = payController.getPay(param.getCurrentDayProceedParam()).getData();

        Date now = new Date();
        List<Pay> currentDayFrees = payService.list(new LambdaQueryWrapper<Pay>()
                .eq(Pay::getCreator, LoginUser.getId())
                .and(ext -> ext
                        .ge(Pay::getCreateTime, DateUtil.beginOfDay(now))
                        .lt(Pay::getCreateTime, DateUtil.endOfDay(now))
                )
                .eq(Pay::getState, 1)
        );
        BigDecimal countFree = currentDayFrees.stream().map(Pay::getFee).reduce(BigDecimal.ZERO, BigDecimal::add);
        return Result.success(new VO(admissionLog, currentDayAdmissionLog, currentDayProceeds, countFree));
    }
}
