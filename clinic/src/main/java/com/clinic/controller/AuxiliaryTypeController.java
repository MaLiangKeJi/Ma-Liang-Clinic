package com.clinic.controller;


import com.bbs.Result;
import com.clinic.entity.AuxiliaryType;
import com.clinic.service.AuxiliaryTypeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class AuxiliaryTypeController {

    @Resource
    private AuxiliaryTypeService auxiliaryTypeService;

    @GetMapping("/auxiliary/type")
    public Result<List<AuxiliaryType>> get(@RequestParam(required = false) String name, @RequestParam(required = false) String unitName) {
        return Result.success(auxiliaryTypeService.search(name, unitName));
    }
}
