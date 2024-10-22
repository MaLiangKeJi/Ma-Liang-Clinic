package com.clinic.controller;

import com.bbs.Result;
import com.clinic.entity.Tag;
import com.clinic.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 标签
 */
@RestController
@RequestMapping("/tag")
public class TagController {

    private TagService service;

    @GetMapping
    public Result getTag(Tag tag) {
        return service.selectTag(tag);
    }
    @Autowired
    public TagController(TagService service) {
        this.service = service;
    }
}
