package com.hawk;


import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONUtil;
import com.hawk.admin.orm.entity.SysUser;
import com.hawk.admin.orm.service.SysUserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Main.class})
public class MainTest {

    @Resource
    private SysUserService sysUserService;

    @Test
    public void test1() {
        log.info("list:{}", JSONUtil.toJsonStr(sysUserService.listAll()));
    }

    @Test
    public void test2(){
        SysUser sysUser = new SysUser();
        sysUser.setUserName("王朝");
        sysUser.setAccount("wangchao");
        sysUser.setPassword(BCrypt.hashpw("123456"));
        sysUser.setDeptId(1L);
        sysUser.setDelFlag(0);
        sysUserService.save(sysUser);
    }
}