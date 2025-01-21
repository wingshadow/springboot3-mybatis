package com.hawk.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hawk.system.entity.SysRole;
import com.hawk.system.entity.SysUser;
import com.hawk.mybatis.annotation.DataScope;
import com.hawk.mybatis.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2023-02-14 14:53
 */
public interface SysUserMapper extends BaseMapperPlus<SysUser> {
    List<SysRole> selectRoleByUserId(Long userId);

    List<SysUser> getAllUser(SysUser sysUser);

    SysUser selectUserByAccount(String userAccount);

    @DataScope(deptAlias = "d")
    Page<SysUser> selectUserList(@Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper,
                                 @Param("page") Page<SysUser> page);

    @DataScope(deptAlias = "d")
    List<SysUser> selectUserList(@Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper);

    @DataScope(deptAlias = "d")
    Page<SysUser> selectAllocatedList(@Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper,
                                      @Param("page") Page<SysUser> page);

    @DataScope(deptAlias = "d")
    Page<SysUser> selectUnallocatedList(@Param(Constants.WRAPPER) Wrapper<SysUser> queryWrapper,
                                      @Param("page") Page<SysUser> page);
}