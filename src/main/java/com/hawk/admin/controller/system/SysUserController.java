package com.hawk.admin.controller.system;

import cn.hutool.core.bean.BeanUtil;
import com.hawk.admin.controller.system.form.SysUserForm;
import com.hawk.common.core.domain.entity.SysUser;
import com.hawk.common.web.page.PageInfo;
import com.hawk.common.web.page.PageQuery;
import com.hawk.common.web.resp.R;
import com.hawk.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping(value = "/list")
    private PageInfo<SysUser> list(SysUserForm form) {
        SysUser sysUser = BeanUtil.copyProperties(form, SysUser.class);
        return userService.selectPageUserList(sysUser, form.getPageSize(), form.getPageNum());
    }

}