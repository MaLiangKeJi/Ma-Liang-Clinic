package com.clinic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinic.entity.StockOut;
import com.clinic.service.StockOutService;
import com.clinic.mapper.StockOutMapper;
import org.springframework.stereotype.Service;

/**
* @author 路晨霖
* @description 针对表【stock_out(库存：出库)】的数据库操作Service实现
* @createDate 2023-09-20 08:30:04
*/
@Service
public class StockOutServiceImpl extends ServiceImpl<StockOutMapper, StockOut>
    implements StockOutService{

}




