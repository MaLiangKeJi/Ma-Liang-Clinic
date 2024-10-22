package com.clinic.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.clinic.dto.AddOrEditPatientVo;
import com.clinic.dto.param.AddPatientParam;
import com.clinic.dto.param.EditPatientParam;
import com.clinic.dto.param.PatientParam;
import com.clinic.entity.Patient;
import com.clinic.service.PatientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 病人
 */
@RestController
@RequestMapping("/patient")
public class PatientController {

    private PatientService service;

    /**
     * 病人新增
     * @param patient 入参
     * @return 添加成功
     */
    @PutMapping
    public Result<AddOrEditPatientVo> addPatient(@RequestBody @Valid AddPatientParam patient){
        return service.add(patient);
    }

    /**
     * 病人修改
     * @param patient 入参
     * @return 修改成功
     */
    @PostMapping
    public Result<Long> updatePatient(@RequestBody @Valid EditPatientParam patient){
        return service.edit(patient);
    }


    /**
     * 病人查询
     */
    @GetMapping
    public Result<Page<Patient>> selectPatient(PatientParam patient){
        return service.select(patient);
    }



    @Resource
    public void setService(PatientService service) {
        this.service = service;
    }
}