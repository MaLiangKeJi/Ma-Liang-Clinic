package com.bbs.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName(value ="role_page")
public class RolePage {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long roleId;

    @TableField(value = "resource_page_id")
    private Long resourcePageId;
}
