package com.clinic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinic.entity.DrugOrder;
import com.clinic.service.DrugOrderService;
import com.clinic.mapper.DrugOrderMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class DrugOrderServiceImpl extends ServiceImpl<DrugOrderMapper, DrugOrder>
    implements DrugOrderService{

}




