package com.hawk.admin.orm.service.impl;

import com.hawk.admin.orm.dao.SysRoleUserMapper;
import com.hawk.common.core.domain.entity.SysRoleUser;
import com.hawk.admin.orm.service.SysRoleUserService;
import com.hawk.mybatis.common.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-26 15:10
 */
@Service
public class SysRoleUserServiceImpl extends BaseServiceImpl<SysRoleUserMapper, SysRoleUser> implements SysRoleUserService {
}
