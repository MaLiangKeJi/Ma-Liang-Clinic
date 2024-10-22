package com.clinic.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.bbs.Result;
import com.clinic.dto.param.QueryDiagnosisProofParam;
import com.clinic.dto.vo.DiagnosisProofFileVo;
import com.clinic.entity.DiagnosisProof;

/**
 *
 */
public interface DiagnosisProofService extends IService<DiagnosisProof> {

    Result<Long> add(DiagnosisProof param);

    Result<Page<DiagnosisProof>> queryList(QueryDiagnosisProofParam param);

    DiagnosisProofFileVo getFileInfo(Long id);
}
