package com.hawk.admin.orm.service.impl;

import com.github.pagehelper.PageInfo;
import com.hawk.admin.orm.dao.SysUserMapper;
import com.hawk.admin.orm.entity.SysRole;
import com.hawk.admin.orm.entity.SysUser;
import com.hawk.admin.orm.service.SysUserService;
import com.hawk.mybatis.common.database.impl.BaseServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2023-02-14 15:01
 */
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public PageInfo<SysUser> listByPage(SysUser paramBean, final int pageNum, final int pageSize) {
        PageInfo<SysUser> pageInfo = super.listByPage(paramBean, pageNum, pageSize);
        List<SysUser> list = pageInfo.getList();
        pageInfo.setList(list);
        return pageInfo;
    }

    @Override
    public List<SysUser> getAllUser(SysUser sysUser) {
        return baseMapper.getAllUser(sysUser);
    }

    public List<SysRole> selectRoleByUserId(Long userId) {
        return baseMapper.selectRoleByUserId(userId);
    }
}