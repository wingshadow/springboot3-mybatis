package com.hawk;


import cn.hutool.core.convert.Convert;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.json.JSONUtil;
import com.hawk.common.core.domain.entity.*;
import com.hawk.framework.service.DeptService;
import com.hawk.system.mapper.SysUserMapper;
import com.hawk.system.service.*;
import com.hawk.utils.StreamUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class MainTest {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysDeptService sysDeptService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysRoleDeptService sysRoleDeptService;

    @Resource
    private SysUserRoleService sysUserRoleService;


    @Resource
    private DeptService deptService;

    @Resource
    private SysUserMapper userMapper;

    @Resource
    private SysConfigService configService;

    @Test
    public void test1() {
        log.info("list:{}", JSONUtil.toJsonStr(sysUserService.listAll()));
    }

    @Test
    public void test2(){
        SysUser sysUser = new SysUser();
        sysUser.setUserName("郭嘉");
        sysUser.setUserAccount("guojia");
        sysUser.setPassword(BCrypt.hashpw("123456"));
        sysUser.setDeptId(1784406554567671810L);
        sysUser.setDelFlag(0);
        sysUserService.save(sysUser);
    }

    @Test
    public void test3(){
        SysDept sysDept = new SysDept();
        sysDept.setDeptName("综合处");
        sysDept.setParentId(1784405825652178946L);
        sysDept.setAncestors("0,1,1784405825652178946");
        sysDept.setDelFlag(0);
        sysDeptService.save(sysDept);
    }
    @Test
    public void test4(){
        SysRole role = new SysRole();
        role.setRoleName("本人");
        /**
         * 1:所有人员
         * 2:自定义部门人员
         * 3:本部门人员
         * 4:本部门及下属部门人员
         * 5:本人信息
         */
        role.setDataScope("5");
        sysRoleService.save(role);
    }

    @Test
    public void test5(){
        SysUserRole roleUser = new SysUserRole();
        roleUser.setRoleId(1784409023771230210L);
        roleUser.setUserId(1787008978792910849L);
        sysUserRoleService.save(roleUser);
    }

    @Test
    public void test6(){
//        String ids1 = sysDeptService.getDeptAndChild(1L);
//        List<Long> ids2 = deptService.deptByParent(1L);
//        log.info("ids1:{}", ids1);
//        log.info("ids2:{}", Convert.toStr(StreamUtils.join(ids2, Convert::toStr)));
        sysDeptService.selectNormalChildrenDeptById(1L);
    }

    @Test
    public void insertBatch(){
        List<SysUser> list = new ArrayList<>();
        SysUser user1 = new SysUser();
        user1.setUserAccount("lisi");

        SysUser user2 = new SysUser();
        user2.setUserAccount("zhangsan");

        list.add(user1);
        list.add(user2);
        userMapper.insertBatch(list);

    }

    @Test
    public void test7(){
        List<SysConfig> list = configService.listAll();
        log.info("{}", JSONUtil.toJsonStr(list));
    }

    public static void main(String[] args) {
        Integer status = 1;
        System.out.println(status.toString());
    }
}