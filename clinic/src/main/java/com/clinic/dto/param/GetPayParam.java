package com.clinic.dto.param;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.vo.BaseParam;
import com.clinic.dto.PayAndRecordPageDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

import static java.util.Objects.nonNull;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class GetPayParam extends BaseParam {

    /**
     * 姓名
     */
    private String name;

    /**
     * 手机号
     */
    private Long phone;

    /**
     * 状态
     */
    private Integer state;

    private Date startDate;

    private Date endDate;

    private String address;

    public GetPayParam(Page<PayAndRecordPageDto> page, String val, Long startDateLong, Long endDateLong) {
        super((int)page.getCurrent(), (int)page.getSize());
        this.name = val;
        if(NumberUtil.isNumber(val)) this.phone = Long.valueOf(val);
        if(nonNull(startDateLong)) this.startDate = new Date(startDateLong);
        this.endDate = nonNull(endDateLong) ? new Date(endDateLong) : this.startDate;
        this.address = val;
    }
}
