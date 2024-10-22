package com.clinic.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.Result;
import com.clinic.entity.Tag;
import com.clinic.mapper.TagMapper;
import com.clinic.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import static com.baomidou.mybatisplus.core.toolkit.ObjectUtils.isNull;
import static java.util.Objects.nonNull;

@Slf4j
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Override
    public Result selectTag(Tag tag) {
        Long id = tag.getId();
        String name = tag.getName();
        Long userId = tag.getUserId();
        return Result.success(
                lambdaQuery()
                        .eq(Tag::getUserId,userId)
                        .eq(nonNull(id), Tag::getId, id)
                        .like(isNull(id) && StringUtils.isNotEmpty(name), Tag::getName, name)
                        .list()
        );
    }
}
