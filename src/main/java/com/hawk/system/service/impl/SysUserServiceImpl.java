package com.hawk.system.service.impl;

import com.hawk.system.mapper.SysUserMapper;
import com.hawk.common.core.domain.entity.SysRole;
import com.hawk.common.core.domain.entity.SysUser;
import com.hawk.system.service.SysUserService;
import com.hawk.mybatis.common.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2023-02-14 15:01
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public List<SysUser> getAllUser(SysUser sysUser) {
        return baseMapper.getAllUser(sysUser);
    }

    public List<SysRole> selectRoleByUserId(Long userId) {
        return baseMapper.selectRoleByUserId(userId);
    }
}