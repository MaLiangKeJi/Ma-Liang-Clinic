package com.clinic.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.clinic.entity.Dossier;
import com.clinic.service.DossierService;
import com.clinic.util.LoginUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 病历
 */
@RestController
@RequestMapping("/dossier")
public class DossierController {
    @Resource
    private DossierService service;

    /**
     * 病例查询
     * @param id 病人ID
     * @param current 页码
     * @param size 条数
     * @return null
     */
    @GetMapping
    public Result<Page<Dossier>> selectDossier(String id, Integer current, Integer size){
        return service.select(LoginUser.getId(), id, current, size);
    }
}