package com.hawk.framework.service;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hawk.common.enums.LoginType;
import com.hawk.common.enums.UserStatus;
import com.hawk.common.exception.UserException;
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
//        List<SysRole> roleList = userMapper.selectRoleByUserId(sysUser.getUserId());
        LoginUser loginUser = build(sysUser);
        LoginHelper.loginByDevice(loginUser, LoginType.PC);
        return StpUtil.getTokenValue();
    }

    private LoginUser build(SysUser sysUser) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(sysUser.getUserId());
        loginUser.setUserAccount(sysUser.getUserAccount());
        loginUser.setUserName(sysUser.getUserName());
        loginUser.setUserType(sysUser.getUserType());
        loginUser.setRoles(sysUser.getRoles());
        loginUser.setDeptId(sysUser.getDeptId());
        loginUser.setDeptName(ObjectUtil.isNotEmpty(sysUser.getDept()) ? sysUser.getDept().getDeptName() : "");
        loginUser.setDept(sysUser.getDept());
        loginUser.setRoles(sysUser.getRoles());
        loginUser.setPhone(sysUser.getMobile());
        return loginUser;
    }

    private SysUser loadUserByAccount(String userAccount) {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .select(SysUser::getUserId, SysUser::getUserAccount, SysUser::getDelFlag, SysUser::getPassword, SysUser::getStatus)
                .eq(SysUser::getUserAccount, userAccount));
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", userAccount);
            throw new UserException("user.not.exists", userAccount);
        } else if (UserStatus.DISABLE.getCode() == user.getStatus()) {
            log.info("登录用户：{} 已被停用.", userAccount);
            throw new UserException("user.blocked", userAccount);
        }
        return userMapper.selectUserByAccount(user.getUserAccount());
    }

    public void logout() {
        LoginUser loginUser = LoginHelper.getLoginUser();
        if (loginUser == null) {
            return;
        }
        StpUtil.logout();
        log.info("{}退出", ObjectUtil.isNotEmpty(loginUser) ? loginUser.getUserAccount() : "");
    }
}
