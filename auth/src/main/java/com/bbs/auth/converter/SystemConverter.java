package com.bbs.auth.converter;

import com.bbs.auth.app.system.CreateSystem;
import com.bbs.auth.app.system.UpdateAdmin;
import com.bbs.auth.entity.System;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SystemConverter {

    @Mapping(target = "admin", ignore = true)
    System toEntity(CreateSystem.Param param);

    @Mapping(target = "admin", ignore = true)
    System toEntity(UpdateAdmin.Param param);
}
