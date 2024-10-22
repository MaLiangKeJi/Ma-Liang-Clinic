package com.clinic.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.DbRuntimeException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinic.converter.StockConverter;
import com.clinic.dto.QueryStockInDto;
import com.clinic.dto.param.PutStockList;
import com.clinic.dto.param.PutStockParam;
import com.clinic.dto.param.QueryStockInParam;
import com.clinic.entity.StockIn;
import com.clinic.entity.StockInDrug;
import com.clinic.mapper.StockInMapper;
import com.clinic.service.StockInService;
import com.clinic.util.LoginUser;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.alibaba.fastjson2.JSON.toJSONString;

/**
* @author 路晨霖
* @description 针对表【stock_in(库存：入库)】的数据库操作Service实现
* @createDate 2023-09-20 08:29:40
*/
@Slf4j
@Service
public class StockInServiceImpl extends ServiceImpl<StockInMapper, StockIn>
    implements StockInService{

    @Resource
    private StockConverter stockConverter;

    @Override
    public StockIn saveBatch(String no, PutStockList param) throws DbRuntimeException {
        Long uid = LoginUser.getId();
        StockIn stockIn = new StockIn(null, no, param.getTotalCost(), uid, param.getRemark(), null);
        if(save(stockIn)) return stockIn;
        log.error("药品入库【入库单】异常! stockInService::save(stockIn={}; param={}))", toJSONString(stockIn), toJSONString(param));
        throw new DbRuntimeException("药品入库【入库单】异常！");
    }

    @Override
    public Long save(String no, PutStockParam param, Long uid) throws DbRuntimeException {
        StockIn stockIn = new StockIn(null, no, param.getTotalCost(), uid, param.getRemark(), null);
        if(save(stockIn)) return stockIn.getId();
        log.error("药品入库【入库单】异常! stockInService::save(stockIn={}; param={}))", toJSONString(stockIn), toJSONString(param));
        throw new DbRuntimeException("药品入库【入库单】异常！");
    }


    @Override
    public Page<QueryStockInDto> query(QueryStockInParam param) {
        MPJLambdaWrapper<StockIn> wrapper = new MPJLambdaWrapper<StockIn>()
                .selectAll(StockIn.class)
                .selectAssociation(StockInDrug.class, StockIn::getStockInDrugs)
                .leftJoin(StockInDrug.class, StockInDrug::getStockInId, StockIn::getId)
                .eq(StockIn::getUserId, LoginUser.getId())

                //药品名称或生产批号查询
                .and(StrUtil.isNotBlank(param.getName()), e -> e
                        .like(StockInDrug::getName, param.getName())
                        .or()
                        .like(StockInDrug::getBatchNumber, param.getName())
                );

        //入库时间查询
        if (ObjectUtils.isNotEmpty(param.getCreateTimes()) && !param.getCreateTimes().isEmpty()) {
            wrapper.between(StockInDrug::getCreateTime,
                    DateUtil.date(param.getCreateTimes().get(NumberUtils.INTEGER_ZERO)).toJdkDate(),
                    DateUtil.date(param.getCreateTimes().get(NumberUtils.INTEGER_ONE)).toJdkDate());
        }

        Page<StockIn> page = baseMapper.selectJoinPage(param.toPage(), StockIn.class, wrapper);
        return getPageByQuery(page);
    }

    private Page<QueryStockInDto> getPageByQuery(Page<StockIn> page) {
        List<QueryStockInDto> queryList = page.getRecords().stream().map(stockConverter::getQueryStockInDto).collect(Collectors.toList());
        Page<QueryStockInDto> resultPage = new Page<QueryStockInDto>();
        resultPage.setCurrent(page.getCurrent());
        resultPage.setPages(page.getPages());
        resultPage.setSize(page.getSize());
        resultPage.setTotal(page.getTotal());
        resultPage.setRecords(queryList);
        return resultPage;
    }
}




