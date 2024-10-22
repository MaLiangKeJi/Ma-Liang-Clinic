package com.clinic.dto.param;

import com.bbs.vo.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 未收费列表入参
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NoPayRecordParam extends BaseParam {

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
        private Integer state = 0;


}
