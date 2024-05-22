package com.hawk.system.service;

import com.hawk.common.core.domain.entity.SysRole;
import com.hawk.common.web.page.PageInfo;
import com.hawk.system.mapper.SysUserMapper;
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

    List<SysRole> selectRoleByUserId(Long userId);

    PageInfo<SysUser> selectPageUserList(SysUser params, int pageSize, int pageNum);

    PageInfo<SysUser> selectAllocatedList(SysUser params, int pageSize, int pageNum);

    PageInfo<SysUser>  selectUnallocatedList(SysUser params, int pageSize, int pageNum);

    List<SysUser> selectUserList(SysUser user);

    void checkUserDataScope(Long userId);

    boolean checkUserAccountUnique(SysUser user);

    boolean checkPhoneUnique(SysUser user);

    boolean checkEmailUnique(SysUser user);

    void checkUserAllowed(SysUser user);

    int resetPwd(SysUser user);

    int updateUserStatus(SysUser user);

    void insertUserAuth(Long userId, Long[] roleIds);

    void insertUserRole(Long userId, Long[] roleIds);

}