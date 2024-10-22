package com.bbs.auth.converter;

import com.bbs.auth.app.register.Register;
import com.bbs.auth.app.user.Search;
import com.bbs.auth.app.user.Info;
import com.bbs.auth.app.user.Me;
import com.bbs.auth.app.user.update.UpdateUser;
import com.bbs.vo.UserVO;
import com.bbs.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserConverter {

    @Mapping(source = "userName", target = "name")
    User toEntity(Register.Param param);

    @Mapping(target = "phone", ignore = true)
    User toEntity(UpdateUser.Param param);

    @Mapping(target = "failureTokenTime", ignore = true)
    UserVO toVO(User entity);

    Search.VO toSearchUserVO(User entity);

    List<Search.VO> toSearchUserVO(List<User> userList);

    Info.VO toInfoVO(User entity);

    Me.VO toMeVO(User entity);

    com.bbs.api.auth.User toAPIUser(UserVO vo);

    List<com.bbs.api.auth.User> toAPIUser(List<User> entity);
    com.bbs.api.auth.User toAPIUser(User entity);
}
