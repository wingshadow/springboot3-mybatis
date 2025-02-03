package com.hawk.mybatis.aspect;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.hawk.framework.annotation.scope.DataScope;
import com.hawk.framework.dto.RoleDTO;
import com.hawk.framework.model.LoginUser;
import com.hawk.system.entity.SysDept;
import com.hawk.system.entity.SysRole;
import com.hawk.system.service.SysRoleService;
import com.hawk.framework.helper.LoginHelper;
import com.hawk.framework.service.DeptService;
import com.hawk.framework.common.core.entity.BaseEntity;
import com.hawk.mybatis.constant.DataScopeType;
import com.hawk.utils.StreamUtils;
import com.hawk.utils.StringUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-28 15:26
 */
@Slf4j
@Component
//@Aspect
public class AspectDataScope {
    //动态参数名
    private static final String DATA_SCOPE_FIELD = "data_scope";

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private DeptService deptService;

    @Before("@annotation(dataScope)")
    public void before(JoinPoint joinPoint, DataScope dataScope) {
        //清除前端传过来的动态参数
        clearDataScope(joinPoint);

        //拿到登录后的用户凭证
        LoginUser loginUser = LoginHelper.getLoginUser();
        //如果为1 则代表为超级管理员 不用进行权限处理
        if (loginUser.getUserId() == 1L) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        List<RoleDTO> sysRoleList = loginUser.getRoles();

        //获取用户每个角色能够访问的部门信息
        for (RoleDTO sysRole : sysRoleList) {
            //查询每个角色的数据范围 1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限
            String sysRoleDataScope = sysRole.getDataScope();
            if (DataScopeType.DATA_SCOPE_ALL.equals(sysRoleDataScope)) {
                // 1全部数据权限 什么也不做
                return;
            } else if (DataScopeType.DATA_SCOPE_CUSTOM.equals(sysRoleDataScope)) {
                List<SysDept> list = sysRoleService.selectDeptByRoleId(sysRole.getRoleId());

                // 2自定义数据权限 也就是直接在角色部门表 查询
                sb.append(String.format(" OR %s.dept_id in(%s)", dataScope.deptAlias(), StreamUtils.join(list, d->Convert.toStr(d.getDeptId()))));
            } else if (DataScopeType.DATA_SCOPE_DEPT.equals(sysRoleDataScope)) {
                // 3当前用户所在部门数据权限
                sb.append(String.format(" OR %s.dept_id=%d", dataScope.deptAlias(), loginUser.getDeptId()));
            } else if (DataScopeType.DATA_SCOPE_DEPT_AND_CHILD.equals(sysRoleDataScope)) {
                // 4当前用户对应的部门以及用户的子部门
                List<Long> deptIdList = deptService.deptByParent(loginUser.getDeptId());
                sb.append(String.format(" OR %s.dept_id in(%s)", dataScope.deptAlias(), StreamUtils.join(deptIdList, Convert::toStr)));
            } else if (DataScopeType.DATA_SCOPE_SELF.equals(sysRoleDataScope)) {
                // 5只能查看当前用户信息，不能查看此角色部门信息和当前用户下的部门信息
                if (StringUtils.isEmpty(dataScope.userAlias())) {
                    sb.append(" OR 1=0");
                } else {
                    sb.append(String.format(" OR %s.user_id=%d", dataScope.userAlias(), loginUser.getUserId()));
                }
            }
        }
        Object arg = joinPoint.getArgs()[0];
        if (!ObjectUtil.isNull(arg) && arg instanceof BaseEntity baseEntity) {
            //截取开始的 "or "
            baseEntity.getParams().put(DATA_SCOPE_FIELD, " and (" + sb.substring(4) + ")");
        }

    }

    private void clearDataScope(JoinPoint joinPoint) {
        //获取第一个参数
        Object arg = joinPoint.getArgs()[0];
        if (!ObjectUtil.isNull(arg) && arg instanceof BaseEntity baseEntity) {
            baseEntity.getParams().put(DATA_SCOPE_FIELD, "");
        }
    }
}
