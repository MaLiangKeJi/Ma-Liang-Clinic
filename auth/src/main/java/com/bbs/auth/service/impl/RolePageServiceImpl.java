package com.bbs.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bbs.auth.entity.RolePage;
import com.bbs.auth.mapper.RolePageMapper;
import com.bbs.auth.service.RolePageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RolePageServiceImpl extends ServiceImpl<RolePageMapper, RolePage> implements RolePageService {
}
