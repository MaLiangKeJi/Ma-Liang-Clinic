package com.clinic.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.clinic.entity.Dossier;
import com.clinic.mapper.DossierMapper;
import org.springframework.stereotype.Component;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class DossierDao extends ServiceImpl<DossierMapper, Dossier> {

    /**
     * 根据病人id查询病历列表
     * @param patientId 病人id
     * @return DossierList
     */
    public Page<Dossier> selectedById(Long userId, String patientId, Integer pageNo, Integer size){
        return lambdaQuery()
                .eq(Dossier::getUserId,userId)
                .eq(isNotBlank(patientId),Dossier::getPatientId,patientId).orderByDesc(Dossier::getCreateTime)
                .page(new Page<>(pageNo, size));
    }

}
