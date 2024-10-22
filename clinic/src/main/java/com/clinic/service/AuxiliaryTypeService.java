package com.clinic.service;

import com.clinic.entity.AuxiliaryType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface AuxiliaryTypeService extends IService<AuxiliaryType> {

    List<AuxiliaryType> search(String name, String unitName);
}
