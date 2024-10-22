package com.bbs.auth.mapper;

import com.bbs.auth.entity.User;
import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Lenovo
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-07-11 14:46:11
* @Entity com.clinic.entity.Info
*/
@Mapper
public interface UserMapper extends MPJBaseMapper<User> {

}




