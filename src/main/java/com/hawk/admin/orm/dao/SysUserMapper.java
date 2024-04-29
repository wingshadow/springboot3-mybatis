package com.hawk.admin.orm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hawk.admin.orm.entity.SysRole;
import com.hawk.admin.orm.entity.SysUser;

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