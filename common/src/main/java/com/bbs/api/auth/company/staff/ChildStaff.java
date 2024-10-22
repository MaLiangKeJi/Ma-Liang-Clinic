package com.bbs.api.auth.company.staff;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChildStaff implements Serializable {

    private Long id;

    private String name;
}
