package com.hawk.admin.orm.service.impl;

import com.hawk.admin.orm.dao.SysRoleMapper;
import com.hawk.admin.orm.entity.SysRole;
import com.hawk.admin.orm.service.SysRoleService;
import com.hawk.mybatis.common.database.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-26 15:10
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}
