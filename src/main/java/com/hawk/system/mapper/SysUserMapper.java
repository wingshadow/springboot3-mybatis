package com.hawk.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hawk.admin.orm.entity.BizCarInfo;
import com.hawk.common.core.domain.entity.SysRole;
import com.hawk.common.core.domain.entity.SysUser;
import com.hawk.mybatis.annotation.DataScope;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2023-02-14 14:53
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
    List<SysRole> selectRoleByUserId(Long userId);

    List<SysUser> getAllUser(SysUser sysUser);

    SysUser selectUserByAccount(String userAccount);

    @DataScope(deptAlias = "d")
    Page<SysUser> selectPageUserList(@Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper,
                                     @Param("page") Page<SysUser> page);
}