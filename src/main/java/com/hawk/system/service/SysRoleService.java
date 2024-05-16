package com.hawk.system.service;

import com.hawk.system.mapper.SysRoleMapper;
import com.hawk.common.core.domain.entity.SysDept;
import com.hawk.common.core.domain.entity.SysRole;
import com.hawk.mybatis.common.BaseService;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2023-02-14 14:58
 */
public interface SysRoleService extends BaseService<SysRoleMapper, SysRole> {

    List<SysRole> getAllRoleList(SysRole sysRole);

    List<SysDept> selectDeptByRoleId(Long roleId);
}