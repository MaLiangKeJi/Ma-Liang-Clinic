package com.bbs.auth.app.location;

import com.bbs.auth.entity.City;
import com.bbs.auth.service.CityService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping
public class GetCity {

    @Resource
    private CityService service;

    @GetMapping("/location/city")
    public Result<Page<City>> all(
            @RequestParam(required = false) Integer type, @RequestParam(required = false) String name,
            @RequestParam("current") Integer current, @RequestParam("size") Integer size
    ) {
        Page<City> result = service.search(type, name, current, size);
        return Result.success(result);
    }

    @GetMapping("/location/city/{pid}")
    public Result<Page<City>> all(
            @PathVariable Long pid,
            @RequestParam("current") Integer current, @RequestParam("size") Integer size
    ) {
        Page<City> result = service.search(pid, current, size);
        return Result.success(result);
    }
}
