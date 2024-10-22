package com.bbs.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 后台：公司系统关联表
 * @TableName back_system_company
 */
@TableName(value ="back_system_company")
@Data
public class SystemCompany implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 公司ID
     */
    @TableField(value = "company_id")
    private Long companyId;

    /**
     * 系统ID
     */
    @TableField(value = "system_id")
    private Long systemId;

    /**
     * 状态
     */
    @TableField(value = "state")
    private Integer state;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public SystemCompany(Long companyId, Long systemId, Integer state) {
        this.companyId = companyId;
        this.systemId = systemId;
        this.state = state;
    }
}