package com.clinic.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.clinic.dto.param.DrugParam;
import com.clinic.entity.Drug;

import java.util.List;

/**
 *
 */
public interface DrugService extends IService<Drug> {

    List<Drug> selectInfoDistinct();

    Page<Drug> search(DrugParam param);

    List<Drug> search(String val);

    Page<Drug> search(String name, Page<Drug> tPage);

    List<Drug> searchByName(String name);

    boolean get(String approvalNumber);
}
