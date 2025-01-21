package com.hawk.system.mapper;

import com.hawk.system.entity.SysUserRole;
import com.hawk.mybatis.mapper.BaseMapperPlus;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-26 15:09
 */
public interface SysUserRoleMapper extends BaseMapperPlus<SysUserRole> {
    List<Long> selectUserIdsByRoleId(Long roleId);
}
