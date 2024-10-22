package com.clinic.service;

import cn.hutool.core.lang.tree.Tree;
import com.bbs.Result;
import com.clinic.dto.Manufacturer;
import com.clinic.dto.param.DrugParam;
import com.clinic.entity.Drug;
import com.clinic.entity.Unit;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 药品
 * Application Service
 */
public interface AppDrugService {

    Result<List<Drug>> search(DrugParam param);

    /**
     * excel 导入
     * @param file excel
     * @return 导入结果
     */
    Result<Boolean> excelImport(MultipartFile file);

    Result<Manufacturer> searchManufacturer(Manufacturer param);

    Result<List<Unit>> searchAllDefUnit();

    Result<List<Unit>> searchNoRepeatAllDefUnit();

    Result<List<Tree<Integer>>> getDefUnitTrace();
}
