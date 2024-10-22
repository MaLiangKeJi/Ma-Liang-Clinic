package com.clinic.service;

import cn.hutool.db.DbRuntimeException;
import com.clinic.dto.param.PutStockParam;
import com.clinic.entity.Stock;
import com.clinic.entity.StockUnit;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
* @author 路晨霖
* @description 针对表【stock_unit(库存 & 单位中间表)】的数据库操作Service
* @createDate 2023-09-20 10:34:07
*/
public interface StockUnitService extends IService<StockUnit> {

    Boolean save(Map<Stock, List<PutStockParam.StockUnitParam>> incrStockMap) throws DbRuntimeException;
}
