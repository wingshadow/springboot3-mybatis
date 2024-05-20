package com.hawk.admin.controller.system;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.hawk.admin.controller.system.form.SysUserForm;
import com.hawk.common.core.domain.entity.SysRole;
import com.hawk.common.core.domain.entity.SysUser;
import com.hawk.common.web.page.PageInfo;
import com.hawk.common.web.page.PageQuery;
import com.hawk.common.web.resp.R;
import com.hawk.framework.helper.LoginHelper;
import com.hawk.system.service.SysRoleService;
import com.hawk.system.service.SysUserService;
import com.hawk.utils.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2023-02-14 15:06
 */
@RestController
@RequestMapping(value = "/system/user")
public class SysUserController {

    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleService roleService;

    @GetMapping(value = "/list")
    private PageInfo<SysUser> list(SysUserForm form) {
        SysUser sysUser = BeanUtil.copyProperties(form, SysUser.class);
        return userService.selectPageUserList(sysUser, form.getPageSize(), form.getPageNum());
    }

    @GetMapping(value = {"/", "/{userId}"})
    public R<Map<String, Object>> getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        Map<String, Object> ajax = new HashMap<>();
        userService.checkUserDataScope(userId);
        if (ObjectUtil.isNotNull(userId)) {
            if (LoginHelper.isAdmin(userId)) {
                List<SysRole> roles = roleService.getAllRoleList();
                ajax.put("roles", LoginHelper.isAdmin(userId) ? roles : StreamUtils.filter(roles, r -> !r.isAdmin()));
            } else {
                SysUser sysUser = userService.getByPrimaryKey(userId);
                ajax.put("user", sysUser);
                ajax.put("roleIds", StreamUtils.toList(roleService.getRoleByUserId(userId), SysRole::getRoleId));
            }
        }
        return R.ok(ajax);
    }
}