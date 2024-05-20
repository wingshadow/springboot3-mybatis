package com.hawk.system.service;

import com.hawk.common.core.domain.entity.SysUser;
import com.hawk.system.mapper.SysRoleMapper;
import com.hawk.common.core.domain.entity.SysDept;
import com.hawk.common.core.domain.entity.SysRole;
import com.hawk.mybatis.common.BaseService;

import java.util.List;
import java.util.Set;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2023-02-14 14:58
 */
public interface SysRoleService extends BaseService<SysRoleMapper, SysRole> {

    List<SysRole> getAllRoleList();

    List<SysDept> selectDeptByRoleId(Long roleId);

    Set<String> getRolePermission(SysUser user);

    List<SysRole> getRoleByUserId(Long userId);
}