package com.hawk.admin.controller.system;

import cn.hutool.core.bean.BeanUtil;
import com.hawk.admin.controller.system.form.SysRoleForm;
import com.hawk.common.core.controller.BaseController;
import com.hawk.common.core.domain.entity.SysRole;
import com.hawk.common.web.page.PageInfo;
import com.hawk.common.web.resp.R;
import com.hawk.system.service.SysDeptService;
import com.hawk.system.service.SysRoleService;
import com.hawk.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-05-21 14:58
 */
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {

    @Autowired
    private SysRoleService roleService;
    @Autowired
    private SysUserService userService;
    @Autowired
    private SysDeptService deptService;

    @GetMapping("/list")
    public PageInfo<SysRole> list(SysRoleForm form) {
        SysRole sysRole = BeanUtil.copyProperties(form, SysRole.class);
        return roleService.selectRoleList(sysRole, form.getPageSize(),form.getPageNum());
    }

    @GetMapping(value = "/{roleId}")
    public R<SysRole> getInfo(@PathVariable Long roleId) {
        roleService.checkRoleDataScope(roleId);
        return R.ok(roleService.getByPrimaryKey(roleId));
    }

    @PostMapping
    public R<Void> add(@Validated @RequestBody SysRole role) {
        if (!roleService.checkRoleNameUnique(role)) {
            return R.fail("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return R.fail("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        return toAjax(roleService.insertRole(role));

    }
}
