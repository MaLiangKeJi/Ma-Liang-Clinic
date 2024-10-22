package com.clinic.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@TableName(value ="drug_tag")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DrugTag {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long drugId;

    private Long tagId;
}
