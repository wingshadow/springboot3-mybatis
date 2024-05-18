package com.hawk.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hawk.admin.orm.entity.BizCarInfo;
import com.hawk.common.web.page.PageInfo;
import com.hawk.system.mapper.SysUserMapper;
import com.hawk.common.core.domain.entity.SysRole;
import com.hawk.common.core.domain.entity.SysUser;
import com.hawk.system.service.SysUserService;
import com.hawk.mybatis.common.impl.BaseServiceImpl;
import com.hawk.utils.StringUtils;
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
    public List<SysUser> getAllUser(SysUser sysUser) {
        return baseMapper.getAllUser(sysUser);
    }

    @Override
    public List<SysRole> selectRoleByUserId(Long userId) {
        return baseMapper.selectRoleByUserId(userId);
    }

    @Override
    public PageInfo<SysUser> selectPageUserList(SysUser params, int pageSize, int pageNum){
        Page<SysUser> page = new Page<>(pageNum,pageSize);
        QueryWrapper<SysUser> query = Wrappers.query();
        query.likeRight(StringUtils.isNotBlank(params.getUserName()),"u.user_name",params.getUserName())
                .likeRight(StringUtils.isNotBlank(params.getMobile()),"u.mobile",params.getMobile());
        page = baseMapper.selectPageUserList(query,page);
        return PageInfo.build(page);
    }
}