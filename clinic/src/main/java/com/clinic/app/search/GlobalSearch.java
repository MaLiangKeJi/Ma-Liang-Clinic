package com.clinic.app.search;

import cn.hutool.core.date.DateUtil;
import com.bbs.Result;
import com.bbs.api.auth.User;
import com.clinic.app.ai.CloudFlare;
import com.clinic.cache.unit.UnitCache;
import com.clinic.entity.*;
import com.clinic.service.*;
import com.clinic.service.impl.StockServiceImpl;
import com.clinic.util.LoginUser;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;


@RequestMapping
@RestController
public class GlobalSearch {

    @Resource
    private PatientService patientService;

    @Resource
    private DrugService drugService;

    @Resource
    private DrugDetailService drugDetailService;

    @Resource
    private PayService payService;

    @Resource
    private AdmissionLogService admissionLogService;

    @Resource
    private PrescriptionDrugService prescriptionDrugService;

    @Resource
    private StockService stockService;

    @Resource
    private StockBatchService stockBatchService;

    @Resource
    private UnitCache unitCache;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VO {

        private List<Patient> patients;

        private List<Drug> drugs;

        private List<DrugDetail> drugDetails;

        private List<AdmissionLog> admissionLogs;

        private List<Pay> unPays;

        private List<DrugUse> drugUses;
        private List<StockBatch> underStockDrugs;

        private List<StockBatch> aboutExpireStockDrugs;

        private List<StockBatch> expireStockDrugs;

        private List<StockBatch> stockBatches;


        private String answer;  // AI 回答
    }

    @GetMapping("/search")
    public Result<Object> search(@RequestParam String val) {


        VO vo = new VO();
//        try {
//            if(val.equals("我需要最近3天的就诊记录")) {
//                User user = LoginUser.get();
//                vo.setAdmissionLogs(admissionLogService.lambdaQuery()
//                        .eq(AdmissionLog::getUserId, user.getId())
//                        .ge(AdmissionLog::getCreateTime, DateUtil.beginOfDay(DateUtil.parse("2024-08-11", "yyyy-MM-dd")))
//                        .lt(AdmissionLog::getCreateTime, DateUtil.endOfDay(DateUtil.parse("2024-08-13", "yyyy-MM-dd")))
//                        .list());
//                return Result.success(vo);
//            } else if(val.equals("全部欠费记录")) {
//                vo.setUnPays(payService.searchUnPays());
//                return Result.success(vo);
//            } else if(val.equals("给我今年的药品使用情况")) {
//                List<PrescriptionDrug> prescriptionDrugs = prescriptionDrugService.selectJoinList(PrescriptionDrug.class, new MPJLambdaWrapper<PrescriptionDrug>()
//                        .selectAll(PrescriptionDrug.class)
//                        .rightJoin(Prescription.class, Prescription::getId, PrescriptionDrug::getPrescriptionId)
//                        .eq(Prescription::getCreator, LoginUser.getId())
//                        .ge(Prescription::getCreateTime, DateUtil.beginOfYear(new Date()))
//                        .isNotNull(PrescriptionDrug::getName)
//                );
//                List<String> drugNames = new ArrayList<>();
//                List<DrugUse> drugUses = new ArrayList<>();
//                prescriptionDrugs.stream().collect(Collectors.groupingBy(PrescriptionDrug::getName)).entrySet().forEach(drugUse -> {
//                    drugNames.add(drugUse.getKey());
//                    drugUses.add(new DrugUse(drugUse));
//                });
//                return Result.success(new DrugUseVO(drugNames, drugUses));
//            } else if(val.equals("库存不足")) {
//                StockServiceImpl.DrugExpiryGroup drugExpiryGroup = stockService.countAndUpdateDrugExpiryState();
//                // 填充 Stock
//                List<StockBatch> stockShortage = drugExpiryGroup.getStockShortage();
//                if(stockShortage.size() > INTEGER_ZERO) {
//                    Map<Long, Stock> stockMap = stockService.listByIds(stockShortage.stream().map(StockBatch::getStockId).collect(Collectors.toSet()))
//                            .stream().collect(Collectors.toMap(Stock::getId, stock -> stock));
//                    stockShortage.forEach(stockBatch -> stockBatch.setStock(stockMap.get(stockBatch.getStockId())));
//                }
//
//                vo.setUnderStockDrugs(drugExpiryGroup.getStockShortage());
//                return Result.success(vo);
//            } else if(val.equals("即将过期")) {
//                return Result.success(expiryDrugCount());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
        if(StringUtils.isNotBlank(val)) {
            if(isNumeric(val)) {
                vo.setPatients(patientService.selectListByPhone(val)); //查病人手机号
                vo.setDrugs(drugService.search(val));
            } else {
                vo.setPatients(patientService.selectByName(val));  //搜病人
                vo.setDrugs(drugService.searchByName(val));    //搜药品
                List<StockBatch> stockBatches = stockBatchService.search(val);  //查库存
                if(stockBatches.size() > INTEGER_ZERO) {
                    Set<Long> stockIds = new HashSet<>();
                    Set<Integer> unitIds = new HashSet<>();
                    stockBatches.forEach(stockBatch -> {
                        stockIds.add(stockBatch.getStockId());
                        unitIds.add(stockBatch.getUnitId());
                    });
                    Map<Long, Stock> stockMap = stockService.listByIds(stockIds)
                            .stream().collect(Collectors.toMap(Stock::getId, stock -> stock));
                    Map<Integer, Unit> unitMap = unitCache.getUnitMap(unitIds);
                    stockBatches.forEach(stockBatch -> {
                        stockBatch.setStock(stockMap.get(stockBatch.getStockId()));
                        stockBatch.setUnit(unitMap.get(stockBatch.getUnitId()));
                    });
                }
                vo.setStockBatches(stockBatches);
            }
        }
        return Result.success(vo);
    }

    /**
     * 统计过期、临期库存药品
     */
    private Collection<DrugExpiryState> expiryDrugCount() {
        StockServiceImpl.DrugExpiryGroup drugExpiryGroup = stockService.countAndUpdateDrugExpiryState();

        // 对于即将过期、和已过期的库存药品，需要查询库存中有没有这个药品同厂商其他批次，如果没有，再查看有没有其他厂商的同名药品
        List<StockBatch> expireStockDrug = drugExpiryGroup.getExpires();
        List<StockBatch> aboutExpireStockDrug = drugExpiryGroup.getAboutExpires();
        Map<Long, List<StockBatch>> normalStockDrugMap = drugExpiryGroup.getNormal().stream().collect(Collectors.groupingBy(StockBatch::getStockId));
        expireStockDrug.addAll(aboutExpireStockDrug);
        Map<Long, DrugExpiryState> result = new HashMap<>();
        Map<Long, List<StockBatch>> allExpireStateNotNormalStockBatchMap = expireStockDrug.stream().collect(Collectors.groupingBy(StockBatch::getStockId));
        allExpireStateNotNormalStockBatchMap.forEach((stockId, allManufacturerStockBatches) -> {
            boolean isExistsDifferentManufacturerDrugs = false;             //不同厂商
            boolean isExistsSameManufacturerDifferentBatchDrugs = false;    //相同厂商不同批次
            List<StockBatch> normalStockDrugs = normalStockDrugMap.get(stockId);
            if(isNull(normalStockDrugs) || normalStockDrugs.size() == INTEGER_ZERO) {
                // 同名且状态正常的药品库存为 0
                DrugExpiryState drugExpiryState = result.getOrDefault(stockId, new DrugExpiryState());
                drugExpiryState.setStockId(stockId);
                drugExpiryState.setIsExistsSameManufacturerDifferentBatchDrugs(false);
                drugExpiryState.setIsExistsDifferentManufacturerDrugs(false);
                result.putIfAbsent(stockId, drugExpiryState);
            } else {
                Map<String, List<StockBatch>> normalStockDrugManufacturerBatchMap = normalStockDrugs.stream().collect(Collectors.groupingBy(StockBatch::getManufacturer));
                // xxx 药品根据厂商遍历 { 厂商名称, List<过期批次> }
                Map<String, List<StockBatch>> manufacturerStockDrugBatchMap = allManufacturerStockBatches.stream().collect(Collectors.groupingBy(StockBatch::getManufacturer));
                for (Map.Entry<String, List<StockBatch>> manufacturerStockDrugBatchMapEntry : manufacturerStockDrugBatchMap.entrySet()) {
                    String manufacturer = manufacturerStockDrugBatchMapEntry.getKey();
                    List<StockBatch> stockDrugBatchList = manufacturerStockDrugBatchMapEntry.getValue();
                    if(stockDrugBatchList.size() > INTEGER_ZERO) {
                        List<StockBatch> normalStockDrug = normalStockDrugManufacturerBatchMap.get(manufacturer);
                        if(nonNull(normalStockDrug) && normalStockDrug.size()  > INTEGER_ZERO) {
                            // 【同个厂商】有【状态正常】的其他批次
                            isExistsSameManufacturerDifferentBatchDrugs = true;
                            break;
                        }
                        // 【同个厂商】没有【状态正常】的其他批次
                        // 需要查看其他厂商的同名药品，有没有【状态正常】的批次
                        // 默认 False 不需要再设置
                    }
                }
                // 如果不存在【同个厂商】有【状态正常】的其他批次，就检查其他厂商有没有【状态正常】的批次
                if(!isExistsSameManufacturerDifferentBatchDrugs) {
                    for (Map.Entry<String, List<StockBatch>> stateNormalOtherManufacturerStockDrugBatchMapEntry : normalStockDrugManufacturerBatchMap.entrySet()) {
                        List<StockBatch> stockBatchList = stateNormalOtherManufacturerStockDrugBatchMapEntry.getValue();
                        if(nonNull(stockBatchList) && stockBatchList.size() > INTEGER_ZERO) {
                            isExistsDifferentManufacturerDrugs = true;
                            break;
                        }
                    }
                }
            }
            DrugExpiryState drugExpiryState = result.getOrDefault(stockId, new DrugExpiryState());
            drugExpiryState.setStockId(stockId);
            drugExpiryState.setIsExistsDifferentManufacturerDrugs(isExistsDifferentManufacturerDrugs);
            drugExpiryState.setIsExistsSameManufacturerDifferentBatchDrugs(isExistsSameManufacturerDifferentBatchDrugs);
            drugExpiryState.setStockBatches(allManufacturerStockBatches);
            result.putIfAbsent(stockId, drugExpiryState);
        });
        // 回填 Stock
        Set<Long> stockIds = allExpireStateNotNormalStockBatchMap.keySet();
        Collection<DrugExpiryState> drugExpiryStates = result.values();
        if(stockIds.size() > INTEGER_ZERO) {
            Map<Long, Stock> stockMap = stockService.listByIds(stockIds).stream()
                    .collect(Collectors.toMap(Stock::getId, stock -> stock));
            drugExpiryStates.forEach(drugExpiryState -> {
                Stock stock = stockMap.get(drugExpiryState.getStockId());
                drugExpiryState.setName(stock.getName());
                drugExpiryState.setStock(stock);
            });
        }
        return drugExpiryStates;
    }

    private static boolean isAlphaNumeric(String str) {
        return str.matches("^[a-zA-Z]+$|^[0-9]+$|^[a-zA-Z0-9]+$");
    }

    private static boolean isNumeric(String str) {
        return str.matches("^[0-9]+$");
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class DrugUseVO {

        private List<String> drugNames;

        private List<DrugUse> useList;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class DrugUse {

        private String name;

        private Long value;   // 使用数量

        private String unitName;

        private List<PrescriptionDrug> prescriptionDrugs;

        public DrugUse(Map.Entry<String, List<PrescriptionDrug>> entry) {
            List<PrescriptionDrug> prescriptionDrugs = entry.getValue();
            PrescriptionDrug one = prescriptionDrugs.get(INTEGER_ZERO);
            long count = entry.getValue().stream().mapToLong(PrescriptionDrug::getQuantity).sum();
            this.name = entry.getKey() + " (" + count + one.getSingleDoseUnit() +")";
            this.value = count;
            this.unitName = one.getQuantityUnit();
            this.prescriptionDrugs = prescriptionDrugs;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class DrugExpiryState {

        private Long stockId;

        private String name;

        private Boolean isExistsSameManufacturerDifferentBatchDrugs;    //相同厂商不同批次

        private Boolean isExistsDifferentManufacturerDrugs;             //不同厂商其他批次

        private List<StockBatch> stockBatches;

        private Stock stock;
    }
}
