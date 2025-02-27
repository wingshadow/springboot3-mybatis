package com.hawk.framework.service;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hawk.framework.common.constant.CacheConstants;
import com.hawk.framework.common.constant.Constants;
import com.hawk.framework.dto.RoleDTO;
import com.hawk.framework.enums.LoginType;
import com.hawk.framework.enums.LoginWay;
import com.hawk.framework.enums.UserStatus;
import com.hawk.framework.exception.user.CaptchaException;
import com.hawk.framework.exception.user.CaptchaExpireException;
import com.hawk.framework.exception.user.UserException;
import com.hawk.framework.model.LoginUser;
import com.hawk.system.mapper.SysUserMapper;
import com.hawk.system.entity.SysUser;
import com.hawk.framework.helper.LoginHelper;
import com.hawk.system.service.SysConfigService;
import com.hawk.system.service.SysMenuService;
import com.hawk.system.service.SysRoleService;
import com.hawk.utils.StringUtils;
import com.hawk.utils.redis.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-28 16:22
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysLoginService {

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysMenuService menuService;

    private final SysConfigService configService;
    @Autowired
    private SysRoleService roleService;

    @Value("${user.password.maxRetryCount}")
    private Integer maxRetryCount;

    @Value("${user.password.lockTime}")
    private Integer lockTime;

    public String login(String userAccount, String password) {
        SysUser sysUser = loadUserByUsername(userAccount);
        assert sysUser != null;
        if (!BCrypt.checkpw(password, sysUser.getPassword())) {
            throw new UserException("user.password.error");
        }
//        List<SysRole> roleList = userMapper.selectRoleByUserId(sysUser.getUserId());
        LoginUser loginUser = buildLoginUser(sysUser);
        LoginHelper.loginByDevice(loginUser, LoginWay.PC);
        return StpUtil.getTokenValue();
    }

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        // 验证码开关
        if (captchaEnabled) {
            validateCaptcha(username, code, uuid);
        }
        SysUser user = loadUserByUsername(username);
        checkLogin(LoginType.PASSWORD, username, () -> !BCrypt.checkpw(password, user.getPassword()));
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = buildLoginUser(user);
        // 生成token
        LoginHelper.loginByDevice(loginUser, LoginWay.PC);
        return StpUtil.getTokenValue();
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    public void validateCaptcha(String username, String code, String uuid) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.defaultString(uuid, "");
        String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
    }

    private LoginUser buildLoginUser(SysUser sysUser) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(sysUser.getUserId());
        loginUser.setUserAccount(sysUser.getUserAccount());
        loginUser.setUserName(sysUser.getUserName());
        loginUser.setUserType(sysUser.getUserType());
        loginUser.setDeptId(sysUser.getDeptId());
        loginUser.setDeptName(ObjectUtil.isNotEmpty(sysUser.getDept()) ? sysUser.getDept().getDeptName() : "");
        loginUser.setDept(sysUser.getDept());
        loginUser.setPhone(sysUser.getMobile());
        loginUser.setMenuPermission(menuService.getMenuPermission(sysUser));
        loginUser.setRolePermission(roleService.getRolePermission(sysUser));
        List<RoleDTO> roles = BeanUtil.copyToList(sysUser.getRoles(), RoleDTO.class);
        loginUser.setRoles(roles);

        return loginUser;
    }

    private SysUser loadUserByUsername(String userAccount) {
        SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .select(SysUser::getUserId, SysUser::getUserAccount, SysUser::getIsDeleted, SysUser::getPassword, SysUser::getStatus)
                .eq(SysUser::getUserAccount, userAccount));
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", userAccount);
            throw new UserException("user.not.exists", userAccount);
        } else if (UserStatus.DISABLE.getCode() == Integer.parseInt(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", userAccount);
            throw new UserException("user.blocked", userAccount);
        }
        return userMapper.selectUserByAccount(user.getUserAccount());
    }

    /**
     * 登录校验
     */
    private void checkLogin(LoginType loginType, String username, Supplier<Boolean> supplier) {
        String errorKey = CacheConstants.PWD_ERR_CNT_KEY + username;
        String loginFail = Constants.LOGIN_FAIL;

        // 获取用户登录错误次数(可自定义限制策略 例如: key + username + ip)
        Integer errorNumber = RedisUtils.getCacheObject(errorKey);
        // 锁定时间内登录 则踢出
        if (ObjectUtil.isNotNull(errorNumber) && errorNumber.equals(maxRetryCount)) {
            throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
        }

        if (supplier.get()) {
            // 是否第一次
            errorNumber = ObjectUtil.isNull(errorNumber) ? 1 : errorNumber + 1;
            // 达到规定错误次数 则锁定登录
            if (errorNumber.equals(maxRetryCount)) {
                RedisUtils.setCacheObject(errorKey, errorNumber, Duration.ofMinutes(lockTime));
                throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
            } else {
                // 未达到规定错误次数 则递增
                RedisUtils.setCacheObject(errorKey, errorNumber);
                throw new UserException(loginType.getRetryLimitCount(), errorNumber);
            }
        }

        // 登录成功 清空错误次数
        RedisUtils.deleteObject(errorKey);
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
