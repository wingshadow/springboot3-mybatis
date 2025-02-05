package com.hawk.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hawk.framework.common.constant.UserConstants;
import com.hawk.framework.exception.ServiceException;
import com.hawk.framework.helper.DataBaseHelper;
import com.hawk.framework.helper.LoginHelper;
import com.hawk.framework.mybatis.common.impl.BaseServiceImpl;
import com.hawk.framework.web.page.PageInfo;
import com.hawk.system.entity.SysDept;
import com.hawk.system.entity.SysRole;
import com.hawk.system.entity.SysUser;
import com.hawk.system.entity.SysUserRole;
import com.hawk.system.mapper.SysDeptMapper;
import com.hawk.system.mapper.SysUserMapper;
import com.hawk.system.mapper.SysUserRoleMapper;
import com.hawk.system.service.SysUserService;
import com.hawk.utils.StreamUtils;
import com.hawk.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2023-02-14 15:01
 */
@Slf4j
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    @Autowired
    private SysDeptMapper deptMapper;

    @Override
    public List<SysUser> getAllUser(SysUser sysUser) {
        return baseMapper.getAllUser(sysUser);
    }

    @Override
    public List<SysRole> selectRoleByUserId(Long userId) {
        return baseMapper.selectRoleByUserId(userId);
    }

    private Wrapper<SysUser> buildQueryWrapper(SysUser user) {
        Map<String, Object> params = user.getParams();
        QueryWrapper<SysUser> wrapper = Wrappers.query();
        wrapper.eq("u.is_deleted", UserConstants.USER_RETAIN)
                .eq(ObjectUtil.isNotNull(user.getUserId()), "u.user_id", user.getUserId())
                .like(StringUtils.isNotBlank(user.getUserName()), "u.user_name", user.getUserName())
                .eq(ObjectUtil.isNotEmpty(user.getStatus()), "u.status", user.getStatus())
                .like(StringUtils.isNotBlank(user.getMobile()), "u.mobile", user.getMobile())
                .between(params.get("beginTime") != null && params.get("endTime") != null,
                        "u.create_time", params.get("beginTime"), params.get("endTime"))
                .and(ObjectUtil.isNotNull(user.getDeptId()), w -> {
                    List<SysDept> deptList = deptMapper.selectList(new LambdaQueryWrapper<SysDept>()
                            .select(SysDept::getDeptId)
                            .apply(DataBaseHelper.findInSet(user.getDeptId(), "ancestors")));
                    List<Long> ids = StreamUtils.toList(deptList, SysDept::getDeptId);
                    ids.add(user.getDeptId());
                    w.in("u.dept_id", ids);
                });
        return wrapper;
    }

    @Override
    public PageInfo<SysUser> selectPageUserList(SysUser sysUser, int pageSize, int pageNum) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        Wrapper<SysUser> query = buildQueryWrapper(sysUser);
        page = baseMapper.selectUserList(query, page);
//        List<SysUser> list = baseMapper.selectUserList(query);
//        log.info("{}", JSONUtil.toJsonStr(list));
        return PageInfo.build(page);
    }

    @Override
    public PageInfo<SysUser> selectAllocatedList(SysUser user, int pageSize, int pageNum) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        QueryWrapper<SysUser> wrapper = Wrappers.query();
        wrapper.eq("u.is_deleted", UserConstants.USER_RETAIN)
                .eq(ObjectUtil.isNotNull(user.getRoleId()), "r.role_id", user.getRoleId())
                .like(StringUtils.isNotBlank(user.getUserName()), "u.user_name", user.getUserName())
                .eq(ObjectUtil.isNotEmpty(user.getStatus()), "u.status", user.getStatus())
                .like(StringUtils.isNotBlank(user.getMobile()), "u.mobile", user.getMobile());
        page = baseMapper.selectAllocatedList(wrapper, page);
        return PageInfo.build(page);
    }

    @Override
    public PageInfo<SysUser> selectUnallocatedList(SysUser user, int pageSize, int pageNum) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        List<Long> userIds = userRoleMapper.selectUserIdsByRoleId(user.getRoleId());
        QueryWrapper<SysUser> wrapper = Wrappers.query();
        wrapper.eq("u.is_deleted", UserConstants.USER_RETAIN)
                .and(w -> w.ne("r.role_id", user.getRoleId()).or().isNull("r.role_id"))
                .notIn(CollUtil.isNotEmpty(userIds), "u.user_id", userIds)
                .like(StringUtils.isNotBlank(user.getUserAccount()), "u.user_account", user.getUserAccount())
                .like(StringUtils.isNotBlank(user.getMobile()), "u.mobile", user.getMobile());
        page = baseMapper.selectUnallocatedList(wrapper, page);
        return PageInfo.build(page);
    }

    @Override
    public List<SysUser> selectUserList(SysUser user) {
        return baseMapper.selectUserList(this.buildQueryWrapper(user));
    }

    @Override
    public void checkUserDataScope(Long userId) {
        if (!LoginHelper.isAdmin()) {
            SysUser user = new SysUser();
            user.setUserId(userId);
            List<SysUser> users = this.selectUserList(user);
            if (CollUtil.isEmpty(users)) {
                throw new ServiceException("没有权限访问用户数据！");
            }
        }
    }

    @Override
    public boolean checkUserAccountUnique(SysUser user) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserAccount, user.getUserAccount())
                .ne(ObjectUtil.isNotNull(user.getUserId()), SysUser::getUserId, user.getUserId()));
        return !exist;
    }

    @Override
    public boolean checkPhoneUnique(SysUser user) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getMobile, user.getMobile())
                .ne(ObjectUtil.isNotNull(user.getUserId()), SysUser::getUserId, user.getUserId()));
        return !exist;
    }

    @Override
    public boolean checkEmailUnique(SysUser user) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, user.getEmail())
                .ne(ObjectUtil.isNotNull(user.getUserId()), SysUser::getUserId, user.getUserId()));
        return !exist;
    }

    @Override
    public void checkUserAllowed(SysUser user) {
        if (ObjectUtil.isNotNull(user.getUserId()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }

    @Override
    public int resetPwd(SysUser user) {
        return baseMapper.updateById(user);
    }

    @Override
    public int updateUserStatus(SysUser user) {
        return baseMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserAuth(Long userId, Long[] roleIds) {
        userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, userId));
        insertUserRole(userId, roleIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserRole(Long userId, Long[] roleIds) {
        if (ArrayUtil.isNotEmpty(roleIds)) {
            // 新增用户与角色管理
            List<SysUserRole> list = StreamUtils.toList(Arrays.asList(roleIds), roleId -> {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                return ur;
            });
            userRoleMapper.insertBatch(list);
        }
    }

    @Override
    public boolean registerUser(SysUser user){
        user.setCreateBy(user.getUserAccount());
        user.setUpdateBy(user.getUserAccount());
        return baseMapper.insert(user) > 0;
    }

}