package com.hawk.admin.orm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hawk.common.core.domain.entity.SysDept;
import com.hawk.common.core.domain.entity.SysRole;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-26 15:09
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {
    List<SysRole> getAllRoleList();

    List<SysDept> selectDeptByRoleId(Long roleId);
}
