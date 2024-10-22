package com.clinic.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Manufacturer {

    private Long id;

    private String name;

    @TableField(exist = false)
    private Integer current;

    @TableField(exist = false)
    private Integer size;
}
