package com.clinic.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("drug/lot")
public class DrugLotController {
//
//    private AppDrugService service;
//
//    /**
//     * 删除药品批次
//     */
//    @DeleteMapping()
//    public Result deleteDrugBatch(@RequestBody List<Long> ids){
//        service.deleteByIds(ids);
//        return Result.success();
//    }
//    /**
//     * 添加入库单、药品
//     */
//    @PostMapping("/in")
//    public Result inDrugLot(@RequestBody DrugLotParam param){
//        return Result.success(service.inDrugLot(param));
//    }
//
//
//    /**
//     * 添加出库单、药品
//     * @param
//     * @return
//     */
//    @PostMapping("/out")
//    public Result outDrugLot(@RequestBody DrugLotParam param){
//        return Result.success(service.outDrugLot(param));
//    }
//
//
//    /**
//     * 查询库存药品-按药品
//     * @param
//     * @return
//     */
//    @GetMapping()
//    public Result<List<DrugLotVo>> getDrugLot(@RequestBody DrugDetailParam param){
//        return Result.success(service.selectDrugLot(param));
//    }
//
//
//    @Resource
//    public void setDao(AppDrugService service) {
//        this.service = service;
//    }
}
