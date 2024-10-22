package com.clinic.service;

import cn.hutool.db.DbRuntimeException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.clinic.dto.QueryStockInDto;
import com.clinic.dto.param.PutStockList;
import com.clinic.dto.param.PutStockParam;
import com.clinic.dto.param.QueryStockInParam;
import com.clinic.entity.StockIn;

/**
* @author 路晨霖
* @description 针对表【stock_in(库存：入库)】的数据库操作Service
* @createDate 2023-09-20 08:29:40
*/
public interface StockInService extends IService<StockIn> {

    StockIn saveBatch(String no, PutStockList param) throws DbRuntimeException;


    Long save(String no, PutStockParam param, Long uid) throws DbRuntimeException;

    Page<QueryStockInDto> query(QueryStockInParam param);
}
