package com.hawk.admin.controller.system;

import com.hawk.common.core.domain.entity.SysUser;
import com.hawk.common.web.resp.R;
import com.hawk.system.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "user")
public class UserController {

    @Autowired
    private SysUserService userService;

    @GetMapping(value = "list")
    private R<List<SysUser>> list() {
        return R.ok(userService.list());
    }

}