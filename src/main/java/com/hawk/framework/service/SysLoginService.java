package com.hawk.framework.service;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hawk.system.mapper.SysUserMapper;
import com.hawk.common.core.domain.entity.SysRole;
import com.hawk.common.core.domain.entity.SysUser;
import com.hawk.common.enums.UserType;
import com.hawk.framework.helper.LoginHelper;
import com.hawk.framework.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-28 16:22
 */
@Slf4j
@Service
public class SysLoginService {

    @Autowired
    private SysUserMapper userMapper;

    public String login(String userAccount, String password) {
        SysUser sysUser = loadUserByAccount(userAccount);
        assert sysUser != null;
        if (!BCrypt.checkpw(password, sysUser.getPassword())) {
            return null;
        }

        List<SysRole> roleList = userMapper.selectRoleByUserId(sysUser.getUserId());

        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(sysUser.getUserId());
        loginUser.setUsername(sysUser.getUserAccount());
        loginUser.setNickName(sysUser.getUserName());
        loginUser.setUserType(UserType.employee.getUserType());
        loginUser.setRoles(roleList);
        loginUser.setDeptId(sysUser.getDeptId());

        LoginHelper.loginByDevice(loginUser,"PC");
        return StpUtil.getTokenValue();
    }

    private SysUser loadUserByAccount(String userAccount) {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
//                .select(SysUser::getUserId,SysUser::getUserName, SysUser::getDelFlag, SysUser::getPassword)
                .eq(SysUser::getUserAccount, userAccount));
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", userAccount);
            return null;
        }
        return user;
    }
}
