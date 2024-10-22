package com.bbs.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessageVO {
    private Long id;

    private String name;

    private Long failureTokenTime;
}
