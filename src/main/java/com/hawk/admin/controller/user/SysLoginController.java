package com.hawk.admin.controller.user;

import cn.dev33.satoken.annotation.SaIgnore;
import com.hawk.framework.constant.Constants;
import com.hawk.framework.model.LoginBody;
import com.hawk.framework.service.SysLoginService;
import com.hawk.mybatis.common.web.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-28 16:18
 */
@RestController
public class SysLoginController {

    @Autowired
    private SysLoginService loginService;

    @SaIgnore
    @PostMapping("/login")
    public R<Map<String, Object>> login(@Validated @RequestBody LoginBody loginBody) {
        Map<String, Object> ajax = new HashMap<>();
        // 生成令牌
        String token = loginService.login(loginBody.getAccount(), loginBody.getPassword());
        ajax.put(Constants.TOKEN, token);
        return R.ok(ajax);
    }
}
