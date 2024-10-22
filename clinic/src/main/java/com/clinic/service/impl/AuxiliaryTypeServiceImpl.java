package com.clinic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinic.entity.AuxiliaryType;
import com.clinic.mapper.AuxiliaryTypeMapper;
import com.clinic.service.AuxiliaryTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class AuxiliaryTypeServiceImpl extends ServiceImpl<AuxiliaryTypeMapper, AuxiliaryType>
    implements AuxiliaryTypeService{

    @Override
    public List<AuxiliaryType> search(String name, String unitName) {
        QueryWrapper<AuxiliaryType> wrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(name)) {
            if (name.matches("[a-zA-Z]+")) {
                // 如果是纯英文，进行拼音或首字母模糊匹配
                wrapper.apply("LOWER(CONVERT(name USING gbk)) LIKE LOWER(CONVERT({0} USING gbk)) OR LOWER(name) LIKE LOWER({0})", "%" + name + "%");
            } else {
                // 否则进行普通 LIKE 查询
                wrapper.like("name", name);
            }
        }
        if(StringUtils.isNotBlank(unitName)) {
            if (unitName.matches("[a-zA-Z]+")) {
                // 如果是纯英文，进行拼音或首字母模糊匹配
                wrapper.apply("LOWER(CONVERT(unit_name USING gbk)) LIKE LOWER(CONVERT({0} USING gbk)) OR LOWER(unit_name) LIKE LOWER({0})", "%" + unitName + "%");
            } else {
                // 否则进行普通 LIKE 查询
                wrapper.like("unit_name", unitName);
            }
        }
        return list(wrapper);
    }
}




