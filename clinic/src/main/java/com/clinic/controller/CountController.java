package com.clinic.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bbs.Result;
import com.clinic.entity.*;
import com.clinic.enums.AdmissionStateEnum;
import com.clinic.service.*;
import com.clinic.service.impl.StockServiceImpl;
import com.clinic.util.LoginUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

/**
 * 统计页面
 */
@Slf4j
@RestController("countClinicIndex")
@RequestMapping
public class CountController {

    @Resource
    private SterilizeLogService sterilizeLogService;
    @Resource
    private DisinfectionLogService disinfectionLogService;
    @Resource
    private AdmissionLogService admissionLogService;
    @Resource
    private PayService payService;
    @Resource
    private StockService stockService;

    @GetMapping("/count")
    public Result<VO> count() {
        long queueNumber = 0;
        long currentDayTotalReceptionNumber = 0;
        Date now = new Date();
        // 库存不足
        StockServiceImpl.DrugExpiryGroup drugExpiryGroup = stockService.countAndUpdateDrugExpiryState();
        Map<String, List<AdmissionLog>> map = admissionLogService.list(new LambdaQueryWrapper<AdmissionLog>()
                .eq(AdmissionLog::getUserId, LoginUser.getId())
                .and(ext -> ext
                        .ge(AdmissionLog::getCreateTime, DateUtil.beginOfMonth(now))
                        .lt(AdmissionLog::getCreateTime, DateUtil.beginOfMonth(DateUtil.offsetMonth(now, INTEGER_ONE)))
                )
        ).stream().collect(Collectors.groupingBy(log -> DateUtil.formatDate(log.getCreateTime())));

        if(map.size() > INTEGER_ZERO) {
            List<AdmissionLog> currentDayAdmissionLogs = map.get(DateUtil.formatDate(now));
            if(nonNull(currentDayAdmissionLogs)) {
                for (AdmissionLog log : currentDayAdmissionLogs) {
                    AdmissionStateEnum state = log.getState();
                    if(AdmissionStateEnum.RUN.equals(state)) {
                        queueNumber++;
                    }
                    currentDayTotalReceptionNumber++;
                }
            }
        }
        // 本月接诊人数（折线图数据）
        Map<String, Integer> singularMonthReceptionNumber = map.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entity -> entity.getValue().size()));

        String[] split = DateUtil.formatDate(now).split("-");
        String prefix = split[1] + "-";
        List<String> dateList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();
        int currentMonthDayNumber = DateUtil.lengthOfMonth(DateUtil.month(now), DateUtil.isLeapYear(DateUtil.year(now)));
        int max = 0;
        for (int day = 1; day <= currentMonthDayNumber; day++) {
            String key = prefix + (day < 10 ? ("0" + day) : day);
            dateList.add(key);
            Integer number = singularMonthReceptionNumber.getOrDefault(split[0] + "-" + key, INTEGER_ZERO);
            if(number > max) max = number;
            numberList.add(number);
        }
        ReceptionPeopleNumberChartData receptionPeopleNumberChartData = new CountController.ReceptionPeopleNumberChartData(dateList, numberList, max);

        // 本月销售额（柱状图数据）
        List<Pay> currentMonthPayList = payService.lambdaQuery()
                .eq(Pay::getCreator, LoginUser.getId())
                .and(ext -> ext
                        .ge(Pay::getUpdateTime, DateUtil.beginOfMonth(now))
                        .lt(Pay::getUpdateTime, DateUtil.beginOfMonth(DateUtil.offsetMonth(now, INTEGER_ONE)))
                )
                .eq(Pay::getState, 1)
                .list();
        Map<Date, List<Pay>> singularMonthEveryDayPayMap = currentMonthPayList.stream()
                .collect(Collectors.groupingBy(Pay::getCreateTime));
        Map<String, BigDecimal> singularMonthEveryDayFeeMap = singularMonthEveryDayPayMap.entrySet().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(DateUtil.formatDate(entry.getKey()), entry.getValue()))
                .collect(Collectors.groupingBy(AbstractMap.SimpleEntry::getKey))
                .entrySet().stream()
                .map(entry -> {
                    BigDecimal singularMonthFee = entry.getValue().stream().map(AbstractMap.SimpleEntry::getValue)
                            .flatMap(List::stream).map(Pay::getFee).reduce(BigDecimal.ZERO, BigDecimal::add);
                    return new AbstractMap.SimpleEntry<>(entry.getKey(), singularMonthFee);
                }).collect(Collectors.toMap(
                        AbstractMap.SimpleEntry::getKey,
                        AbstractMap.SimpleEntry::getValue
                ));
        BigDecimal currentDayEarnings = singularMonthEveryDayFeeMap.getOrDefault(DateUtil.formatDate(now), BigDecimal.ZERO);

        List<BigDecimal> singularMonthSalesList = new ArrayList<>();
        BigDecimal maxEarnings = BigDecimal.ZERO;
        for (int day = 1; day <= currentMonthDayNumber; day++) {
            String key = prefix + (day < 10 ? ("0" + day) : day);
            BigDecimal number = singularMonthEveryDayFeeMap.getOrDefault(split[0] + "-" + key, BigDecimal.ZERO);
            if(number.compareTo(maxEarnings) == INTEGER_ONE) maxEarnings = number;
            singularMonthSalesList.add(number);
        }
        SingularMonthSalesChartData singularMonthSalesChartData = new SingularMonthSalesChartData(dateList, singularMonthSalesList, maxEarnings);

        int AboutExpiresDrugNumber = drugExpiryGroup.getAboutExpires().size();
        boolean existCriticalDrug = AboutExpiresDrugNumber > INTEGER_ZERO;
        int stockUnderDrugNumber = drugExpiryGroup.getStockShortage().size();
        boolean existUnderStockDrug = stockUnderDrugNumber  > INTEGER_ZERO;
        boolean isDisinfection = disinfectionLogService.lambdaQuery()
                .eq(DisinfectionLog::getUserId, LoginUser.getId())
                .eq(DisinfectionLog::getCreateTime, now)
                .exists();

        boolean isSterilize = sterilizeLogService.lambdaQuery()
                .eq(SterilizeLog::getUserId, LoginUser.getId())
                .eq(SterilizeLog::getCreateTime, now)
                .exists();
        VO vo = new VO(
                queueNumber,
                currentDayTotalReceptionNumber,
                currentDayEarnings,
                existCriticalDrug,
                existUnderStockDrug,
                isDisinfection,
                isSterilize,
                receptionPeopleNumberChartData,
                singularMonthSalesChartData,
                drugExpiryGroup
        );
        if(existCriticalDrug) vo.setExistCriticalDrugNumber(AboutExpiresDrugNumber);
        if(existUnderStockDrug) vo.setExistCriticalDrugNumber(stockUnderDrugNumber);
        return Result.success(vo);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class VO {

        /**
         * 正在接诊（待接诊数）
         */
        private Long queueNumber;

        /**
         * 今日总接诊数
         */
        private Long currentDayTotalReceptionNumber;

        /**
         * 今日收益
         */
        private BigDecimal currentDayEarnings;

        /**
         * 有无临期药品
         */
        private Boolean existCriticalDrug;

        /**
         * 临期药品数量
         */
        private Integer existCriticalDrugNumber;

        /**
         * 有无库存不足药品
         */
        private Boolean existUnderStockDrug;

        /**
         * 库存不足药品数量
         */
        private Integer existUnderStockDrugNumber;

        /**
         * 今日是否消杀
         */
        private Boolean isDisinfection;

        /**
         * 今日是否消毒
         */
        private Boolean isSterilize;

        private ReceptionPeopleNumberChartData receptionPeopleNumberChartData;

        private SingularMonthSalesChartData singularMonthSalesChartData;

        private StockServiceImpl.DrugExpiryGroup drugExpiryGroup;

        public VO(
                Long queueNumber,
                Long currentDayTotalReceptionNumber,
                BigDecimal currentDayEarnings,
                Boolean existCriticalDrug,
                Boolean existUnderStockDrug,
                Boolean isDisinfection,
                Boolean isSterilize,
                ReceptionPeopleNumberChartData receptionPeopleNumberChartData,
                SingularMonthSalesChartData singularMonthSalesChartData,
                StockServiceImpl.DrugExpiryGroup drugExpiryGroup
        ) {
            this.queueNumber = queueNumber;
            this.currentDayTotalReceptionNumber = currentDayTotalReceptionNumber;
            this.currentDayEarnings = currentDayEarnings;
            this.existCriticalDrug = existCriticalDrug;
            this.existUnderStockDrug = existUnderStockDrug;
            this.isDisinfection = isDisinfection;
            this.isSterilize = isSterilize;
            this.receptionPeopleNumberChartData = receptionPeopleNumberChartData;
            this.singularMonthSalesChartData = singularMonthSalesChartData;
            this.drugExpiryGroup = drugExpiryGroup;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReceptionPeopleNumberChartData {

        private List<String> dateList;

        private List<Integer> numberList;

        private Integer max;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SingularMonthSalesChartData {

        private List<String> dateList;

        private List<BigDecimal> numberList;

        private BigDecimal max;
    }
}
