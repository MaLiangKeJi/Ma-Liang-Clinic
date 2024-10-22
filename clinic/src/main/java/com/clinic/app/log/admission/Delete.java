package com.clinic.app.log.admission;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.clinic.entity.AdmissionLog;
import com.clinic.mapper.AdmissionLogMapper;
import com.github.yulichang.base.MPJBaseServiceImpl;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController("delAdmission")
@RequestMapping
public class Delete extends MPJBaseServiceImpl<AdmissionLogMapper, AdmissionLog> {

    @Resource
    private SearchList searchList;

    @DeleteMapping("/log/admission")
    public Result<Page<AdmissionLog>> delete(@RequestBody SearchList.Param param) {
        removeById(param.getId());
        param.setId(null);
        return searchList.search(param);
    }
}
