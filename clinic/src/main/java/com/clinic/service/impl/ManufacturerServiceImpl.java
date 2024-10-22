package com.clinic.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinic.dto.Manufacturer;
import com.clinic.mapper.ManufacturerMapper;
import com.clinic.service.ManufacturerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class ManufacturerServiceImpl extends ServiceImpl<ManufacturerMapper, Manufacturer> implements ManufacturerService {
    @Override
    public Page<Manufacturer> search(Manufacturer param) {
        return lambdaQuery()
                .eq(Objects.nonNull(param.getId()), Manufacturer::getId, param.getId())
                .like(Objects.isNull(param.getId()) && StringUtils.isNotBlank(param.getName()), Manufacturer::getName, param.getName())
                .page(new Page(param.getCurrent(), param.getSize()));
    }
}
