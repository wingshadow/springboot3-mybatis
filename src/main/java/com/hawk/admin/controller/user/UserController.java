package com.hawk.admin.controller.user;

import com.hawk.admin.orm.entity.SysUser;
import com.hawk.admin.orm.service.SysUserService;
import com.hawk.framework.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2023-02-14 15:06
 */
@RestController
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private SysUserService userService;

}