package com.clinic.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.clinic.app.diagnosis.file.create.CreateDiagnosisProofFile;
import com.clinic.dto.param.QueryDiagnosisProofParam;
import com.clinic.entity.DiagnosisProof;
import com.clinic.service.DiagnosisProofService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping("/proof")
public class DiagnosisProofController {

    private final DiagnosisProofService service;

    @Resource
    private CreateDiagnosisProofFile createDiagnosisProofFile;

    @PutMapping()
    public Result<Long> add(@RequestBody DiagnosisProof param){
        return service.add(param);
    }



    @GetMapping()
    public Result<Page<DiagnosisProof>> getList(QueryDiagnosisProofParam param){
        return service.queryList(param);
    }

    @GetMapping("/one")
    public Result<DiagnosisProof> getFileInfo(@NotNull Long id){
        return Result.success(service.getById(id));
    }

    @GetMapping("/file")
    public void getFile(@NotNull Long id) throws Exception {
        createDiagnosisProofFile.generation(id);
    }

    @DeleteMapping()
    public Result<Boolean> delete(@NotNull Long id){
        return Result.success(service.removeById(id));
    }


    @Autowired
    public DiagnosisProofController(DiagnosisProofService service) {
        this.service = service;
    }
}
