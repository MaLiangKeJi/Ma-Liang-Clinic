package com.clinic.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 药品标签
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    /**
     *  用户ID
     */
    private Long userId;

    private Long id;

    private String name;
}
