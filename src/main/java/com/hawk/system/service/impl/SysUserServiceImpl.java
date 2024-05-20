package com.hawk.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hawk.admin.orm.entity.BizCarInfo;
import com.hawk.common.constant.UserConstants;
import com.hawk.common.core.domain.entity.SysDept;
import com.hawk.common.exception.ServiceException;
import com.hawk.common.web.page.PageInfo;
import com.hawk.framework.helper.LoginHelper;
import com.hawk.system.mapper.SysUserMapper;
import com.hawk.common.core.domain.entity.SysRole;
import com.hawk.common.core.domain.entity.SysUser;
import com.hawk.system.service.SysUserService;
import com.hawk.mybatis.common.impl.BaseServiceImpl;
import com.hawk.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
        wrapper.eq("u.del_flag", UserConstants.USER_NORMAL)
                .eq(ObjectUtil.isNotNull(user.getUserId()), "u.user_id", user.getUserId())
                .like(StringUtils.isNotBlank(user.getUserName()), "u.user_name", user.getUserName())
                .eq(ObjectUtil.isNotEmpty(user.getStatus()), "u.status", user.getStatus())
                .like(StringUtils.isNotBlank(user.getMobile()), "u.mobile", user.getMobile())
                .between(params.get("beginTime") != null && params.get("endTime") != null,
                        "u.create_time", params.get("beginTime"), params.get("endTime"));
        return wrapper;
    }

    @Override
    public PageInfo<SysUser> selectPageUserList(SysUser sysUser, int pageSize, int pageNum){
        Page<SysUser> page = new Page<>(pageNum,pageSize);
        Wrapper<SysUser> query = buildQueryWrapper(sysUser);
        page = baseMapper.selectUserList(query,page);
//        List<SysUser> list = baseMapper.selectUserList(query);
//        log.info("{}", JSONUtil.toJsonStr(list));
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
}