package com.clinic.service.impl;

import cn.hutool.db.DbRuntimeException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinic.dto.param.PutStockParam;
import com.clinic.entity.Stock;
import com.clinic.entity.StockUnit;
import com.clinic.service.StockUnitService;
import com.clinic.mapper.StockUnitMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.alibaba.fastjson2.JSON.toJSONString;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;
import static org.apache.commons.lang3.math.NumberUtils.LONG_ZERO;

/**
* @author 路晨霖
* @description 针对表【stock_unit(库存 & 单位中间表)】的数据库操作Service实现
* @createDate 2023-09-20 10:34:07
*/
@Slf4j
@Service
public class StockUnitServiceImpl extends ServiceImpl<StockUnitMapper, StockUnit>
    implements StockUnitService{

    @Override
    public Boolean save(Map<Stock, List<PutStockParam.StockUnitParam>> incrStockMap) throws DbRuntimeException {
        int size = incrStockMap.keySet().size() * 4;
        List<StockUnit> unitList = new ArrayList<>(size);
        incrStockMap.forEach((stock, units) -> {
            for (int index = INTEGER_ZERO; index < units.size(); index++) {
                PutStockParam.StockUnitParam unit = units.get(index);
                Long stepSize = unit.getStepSize();
                if(index == INTEGER_ZERO) {
                    stepSize = LONG_ZERO;
                }
                unitList.add(new StockUnit(stock.getId(), unit.getId(), index, stepSize));
            }
        });
        if(saveBatch(unitList)) return true;
        log.error("药品入库【单位存储】异常! stockInService::saveUnit(incrStockMap={}))", toJSONString(incrStockMap));
        throw new DbRuntimeException("药品入库【单位存储】异常！");
    }
}




