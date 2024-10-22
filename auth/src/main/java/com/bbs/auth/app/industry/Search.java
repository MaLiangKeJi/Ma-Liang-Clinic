package com.bbs.auth.app.industry;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.bbs.auth.entity.NormIndustry;
import com.bbs.auth.service.NormIndustryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController("searchIndustry")
@RequestMapping
public class Search {

    @Resource
    private NormIndustryService service;

    @GetMapping("/industry")
    public Result<Page<NormIndustry>> search(String name, Integer current, Integer size) {
        return Result.success(service.lambdaQuery()
                .like(StringUtils.isNotBlank(name), NormIndustry::getName, name)
                .page(new Page<>(current, size))
        );
    }
}
