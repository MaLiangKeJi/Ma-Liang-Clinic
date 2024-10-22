package com.clinic.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.util.FirstWordsSqlUtils;
import com.bbs.util.MyStringUtil;
import com.clinic.entity.Usage;
import com.clinic.mapper.UsageMapper;
import com.clinic.service.UsageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static cn.hutool.core.lang.Validator.isWord;
/**
* @author 路晨霖
* @description 针对表【usage(用法)】的数据库操作Service实现
* @createDate 2023-12-14 13:21:24
*/
@Service
public class UsageServiceImpl extends ServiceImpl<UsageMapper, Usage>
    implements UsageService{

    @Override
    public List<Usage> search(String name) {
        LambdaQueryChainWrapper<Usage> wrapper = lambdaQuery();
        if(StringUtils.isNotBlank(name)){
            if(isWord(name)){
                String sql = FirstWordsSqlUtils.getSql(name);
                wrapper.apply(sql);
            }else{
                wrapper.like(Usage::getName, name);
            }
        }
        return wrapper.list();
    }
}




