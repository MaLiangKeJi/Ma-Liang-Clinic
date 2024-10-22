package com.clinic.service.impl;

import cn.hutool.db.DbRuntimeException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.api.auth.User;
import com.clinic.converter.StockConverter;
import com.clinic.dto.param.PutStockList;
import com.clinic.dto.param.PutStockParam;
import com.clinic.entity.StockIn;
import com.clinic.entity.StockInDrug;
import com.clinic.mapper.StockInDrugMapper;
import com.clinic.service.StockBatchService;
import com.clinic.service.StockInDrugService;
import com.clinic.util.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.fastjson2.JSON.toJSONString;

/**
* @author 路晨霖
* @description 针对表【stock_in_drug(库存：入库 & 药品关联)】的数据库操作Service实现
* @createDate 2023-09-20 08:29:57
*/
@Slf4j
@Service
public class StockInDrugServiceImpl extends ServiceImpl<StockInDrugMapper, StockInDrug>
    implements StockInDrugService{


    @Resource
    private StockConverter converter;

    private final StockBatchService stockBatchService;

    private final DataSourceTransactionManager transactionManager;

    private final TransactionDefinition transactionDefinition;

    @Override
    public void saveBatch(String no, PutStockList param, StockIn stockIn) throws DbRuntimeException {
        List<StockInDrug> stockInDrugList = param.getDrugs().stream().map(stock -> stock.getBatchList().stream()
                .map(batch -> new StockInDrug(no,stockIn, stock, batch))
                .collect(Collectors.toList())).flatMap(Collection::stream).distinct().collect(Collectors.toList());
        if(saveBatch(stockInDrugList)) return;
        log.error("药品入库【入库单-具体药品】异常! stockInService::saveStockInDrug(no={}； param={}; stockIn={}))", no, toJSONString(param), toJSONString(stockIn));
        throw new DbRuntimeException("药品入库【入库单-具体药品】异常！");
    }

    @Override
    public Boolean save(String no, PutStockParam param, Long stockInID, Long drugNumber, Long stockBatchID) {
        StockInDrug record = converter.toStockInDrugEntity(param);
        User user = LoginUser.get();
        PutStockParam.StockUnitParam maxUnit = param.getStockUnit().get(param.getStockUnit().size() - 1);
        PutStockParam.StockUnitParam minUnit = param.getStockUnit().get(0);
        record.setNo(no);
        record.setStockBatchId(stockBatchID);
        record.setUserId(user.getId());
        record.setStockInId(stockInID);
        record.setNumber(maxUnit.getStepSize());
        record.setCost(computeCost(param, drugNumber));
        record.setCostUnit(minUnit.getId());
        record.setTotalCost(computeTotalCost(param));
        record.setTotalCostUnit(maxUnit.getId());
        record.setAcceptResult(param.getAcceptResult());
        record.setRemark(param.getRemark());
        record.setCreateName(user.getName());


        TransactionStatus transaction = transactionManager.getTransaction(transactionDefinition);
        try {
            if(save(record) && stockBatchService.updateCost(stockBatchID, record.getCost(), minUnit.getId())) {
                transactionManager.commit(transaction);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        transactionManager.rollback(transaction);
        return false;
    }

    private BigDecimal computeCost(PutStockParam param, Long drugNumber) {
        return param.getTotalCost().divide(new BigDecimal(drugNumber), 2, RoundingMode.UP);
    }

    private BigDecimal computeTotalCost(PutStockParam param) {
        return param.getTotalCost();
    }

    @Autowired
    public StockInDrugServiceImpl(StockBatchService stockBatchService, DataSourceTransactionManager transactionManager, TransactionDefinition transactionDefinition) {
        this.stockBatchService = stockBatchService;
        this.transactionManager = transactionManager;
        this.transactionDefinition = transactionDefinition;
    }
}




