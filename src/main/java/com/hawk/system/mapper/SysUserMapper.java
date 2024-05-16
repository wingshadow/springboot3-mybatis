package com.hawk.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hawk.common.core.domain.entity.SysRole;
import com.hawk.common.core.domain.entity.SysUser;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2023-02-14 14:53
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    List<SysRole> selectRoleByUserId(Long userId);

    List<SysUser> getAllUser(SysUser sysUser);
}