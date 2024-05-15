package com.hawk.admin.orm.service;

import com.hawk.admin.orm.dao.SysUserMapper;
import com.hawk.common.core.domain.entity.SysUser;
import com.hawk.mybatis.common.BaseService;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2023-02-14 14:58
 */
public interface SysUserService extends BaseService<SysUserMapper, SysUser> {
    List<SysUser> getAllUser(SysUser sysUser);
}