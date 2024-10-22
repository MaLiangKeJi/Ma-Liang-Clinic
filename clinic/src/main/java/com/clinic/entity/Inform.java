package com.clinic.entity;

import com.clinic.controller.InformController;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inform implements Serializable {

    private String title;

    private String content;

    private Date time;

    private int type;

    private Boolean isNotRead;

    private static final long serialVersionUID = 1L;

    public Inform(InformController.InformVo informVo) {
        this.title = informVo.getTitle();
        this.content = informVo.getContent();
        this.time = new Date();
        this.type = informVo.getType();
    }
}
