package com.hawk.framework.helper;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.hawk.admin.orm.entity.SysDept;
import com.hawk.framework.constant.UserConstants;
import com.hawk.framework.enums.UserType;
import com.hawk.framework.model.LoginUser;
import com.hawk.framework.service.DeptService;
import com.hawk.utils.SpringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录鉴权助手
 * <p>
 * user_type 为 用户类型 同一个用户表 可以有多种用户类型 例如 pc,app
 * deivce 为 设备类型 同一个用户类型 可以有 多种设备类型 例如 web,ios
 * 可以组成 用户类型与设备类型多对多的 权限灵活控制
 * <p>
 * 多用户体系 针对 多种用户类型 但权限控制不一致
 * 可以组成 多用户类型表与多设备类型 分别控制权限
 *
 * @author Lion Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginHelper {

    public static final String LOGIN_USER_KEY = "loginUser";
    public static final String USER_KEY = "userId";

    private static final Map<Long, List<Long>> deptList = new HashMap<>();

    /**
     * 登录系统
     *
     * @param loginUser 登录用户信息
     */
    public static void login(LoginUser loginUser) {
        loginByDevice(loginUser,"PC");
    }

    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param loginUser 登录用户信息
     */
    public static void loginByDevice(LoginUser loginUser, String loginType) {
        SaStorage storage = SaHolder.getStorage();
        storage.set(LOGIN_USER_KEY, loginUser);
        storage.set(USER_KEY, loginUser.getUserId());
        SaLoginModel model = new SaLoginModel();
        model.setDevice(loginType);
        StpUtil.login(loginUser.getLoginId(), model.setExtra(USER_KEY, loginUser.getUserId()));
        StpUtil.getTokenSession().set(LOGIN_USER_KEY, loginUser);
    }

    /**
     * 获取用户(多级缓存)
     */
    public static LoginUser getLoginUser() {
        LoginUser loginUser = (LoginUser) SaHolder.getStorage().get(LOGIN_USER_KEY);
        if (loginUser != null) {
            return loginUser;
        }
        loginUser = (LoginUser) StpUtil.getTokenSession().get(LOGIN_USER_KEY);
        SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        return loginUser;
    }

    /**
     * 获取用户基于token
     */
    public static LoginUser getLoginUser(String token) {
        return (LoginUser) StpUtil.getTokenSessionByToken(token).get(LOGIN_USER_KEY);
    }

    /**
     * 获取用户id
     */
    public static Long getUserId() {
        Long userId;
        try {
            userId = Convert.toLong(SaHolder.getStorage().get(USER_KEY));
            if (ObjectUtil.isNull(userId)) {
                userId = Convert.toLong(StpUtil.getExtra(USER_KEY));
                SaHolder.getStorage().set(USER_KEY, userId);
            }
        } catch (Exception e) {
            return null;
        }
        return userId;
    }

    /**
     * 获取部门ID
     */
    public static Long getDeptId() {
        return getLoginUser().getDeptId();
    }

    /**
     * 获取用户账户
     */
    public static String getUsername() {
        return getLoginUser().getUsername();
    }

    /**
     * 获取用户昵称
     */
    public static String getNickName() {
        return getLoginUser().getNickName();
    }

    /**
     * 获取用户类型
     */
    public static UserType getUserType() {
        String loginId = StpUtil.getLoginIdAsString();
        return UserType.getUserType(loginId);
    }

    /**
     * 是否为管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    public static boolean isAdmin(Long userId) {
        return UserConstants.ADMIN_ID.equals(userId);
    }

    public static boolean isAdmin() {
        return isAdmin(getUserId());
    }

    /**
     * 当前用户部门权限范围
     *
     * @return 当前登录用户允许查看或操作的部门Id范围  空表示全部部门
     */
    public static List<Long> deptScope() {
        if ("admin".equals(getUsername())) {
            return null;
        }
        SysDept userDept = getLoginUser().getDept();
        if (3701L == userDept.getDeptId() || userDept.getDeptId().toString().startsWith("370100")) {
            // 济南市烟草专卖局 市局用户  查看全部
            return null;
        }
        long deptParent = userDept.getDeptId() < 1000000 ? userDept.getDeptId() : userDept.getDeptId() / 100;
        if (!deptList.containsKey(deptParent)) {
            List<Long> deptIdList;
            try {
                DeptService deptService = SpringUtils.getBean(DeptService.class);
                deptIdList = deptService.deptByParent(deptParent);
            } catch (Exception e) {
                deptIdList = CollUtil.newArrayList(deptParent);
                for (int i = 0; i <= 20; i++) {
                    deptIdList.add(deptParent * 100 + i);
                }
            }
            deptList.put(deptParent, deptIdList);
        }
        return deptList.get(deptParent);
    }

}
