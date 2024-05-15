package com.hawk.admin.orm.service.impl;

import com.hawk.admin.orm.dao.SysRoleMapper;
import com.hawk.common.core.domain.entity.SysDept;
import com.hawk.common.core.domain.entity.SysRole;
import com.hawk.admin.orm.service.SysRoleService;
import com.hawk.mybatis.common.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-26 15:10
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Override
    public List<SysRole> getAllRoleList(SysRole sysRole) {
        return baseMapper.getAllRoleList();
    }

    @Override
    public List<SysDept> selectDeptByRoleId(Long roleId) {
        return baseMapper.selectDeptByRoleId(roleId);
    }
}
