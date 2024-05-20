package com.hawk.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.hawk.common.core.domain.entity.SysUser;
import com.hawk.system.mapper.SysRoleMapper;
import com.hawk.common.core.domain.entity.SysDept;
import com.hawk.common.core.domain.entity.SysRole;
import com.hawk.system.service.SysRoleService;
import com.hawk.mybatis.common.impl.BaseServiceImpl;
import com.hawk.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-26 15:10
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
    @Override
    public List<SysRole> getAllRoleList() {
        return baseMapper.getAllRoleList();
    }

    @Override
    public List<SysDept> selectDeptByRoleId(Long roleId) {
        return baseMapper.selectDeptByRoleId(roleId);
    }

    @Override
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<>();
        if (user.isAdmin()) {
            roles.add("admin");
        } else {
            List<SysRole> perms = this.getRoleByUserId(user.getUserId());
            for (SysRole perm : perms) {
                if (ObjectUtil.isNotNull(perm)) {
                    roles.addAll(StringUtils.splitList(perm.getRoleName().trim()));
                }
            }
        }
        return roles;
    }

    @Override
    public List<SysRole> getRoleByUserId(Long userId) {
        return baseMapper.selectRoleByUserId(userId);
    }
}
