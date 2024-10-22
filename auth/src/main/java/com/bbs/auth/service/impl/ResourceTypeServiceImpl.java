package com.bbs.auth.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bbs.Result;
import com.bbs.auth.entity.Resource;
import com.bbs.auth.entity.ResourceType;
import com.bbs.auth.mapper.ResourceTypeMapper;
import com.bbs.auth.service.ResourceTypeService;
import com.bbs.vo.BaseParam;
import com.github.yulichang.base.MPJBaseServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.springframework.stereotype.Service;

/**
* @author 路晨霖
* @description 针对表【resource_type(资源类型)】的数据库操作Service实现
* @createDate 2023-12-28 18:09:18
*/
@Service
public class ResourceTypeServiceImpl extends MPJBaseServiceImpl<ResourceTypeMapper, ResourceType>
    implements ResourceTypeService {

    @Override
    public Result<Page<ResourceType>> searchJoin(BaseParam param) {
        return Result.success(new MPJLambdaWrapper<>(ResourceType.class)
                .selectAll(ResourceType.class)
                .leftJoin(Resource.class, Resource::getType, ResourceType::getId, ext -> ext
                        .selectCollection(Resource.class, ResourceType::getResources)
                )
                .page(param.toPage(), ResourceType.class)
        );
    }

    @Override
    public Result<Page<ResourceType>> search(BaseParam param) {
        return Result.success(lambdaQuery().page(param.toPage()));
    }
}




