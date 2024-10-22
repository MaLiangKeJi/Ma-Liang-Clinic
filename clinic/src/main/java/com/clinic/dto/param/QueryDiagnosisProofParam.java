package com.clinic.dto.param;

import com.bbs.vo.BaseParam;
import lombok.Data;

@Data
public class QueryDiagnosisProofParam extends BaseParam {

    /**
     * 姓名
     */
    private String val;

    /**
     * 消杀时间
     */
    private String createTime;

}
