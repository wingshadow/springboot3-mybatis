package com.hawk.framework.mybatis.handler;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ConcurrentHashSet;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import com.hawk.framework.annotation.scope.DataScope;
import com.hawk.framework.dto.RoleDTO;
import com.hawk.framework.model.LoginUser;
import com.hawk.system.entity.SysDept;
import com.hawk.system.service.SysRoleService;
import com.hawk.framework.helper.LoginHelper;
import com.hawk.framework.service.DeptService;
import com.hawk.utils.SpringUtils;
import com.hawk.utils.StreamUtils;
import com.hawk.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.hawk.framework.mybatis.constant.DataScopeType.*;


/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-05-04 09:37
 */
@Slf4j
public class DataPermissionHandler {


    /**
     * 方法或类(名称) 与 注解的映射关系缓存
     */
    private final Map<String, DataScope> dataScopeMap = new ConcurrentHashMap<>();

    /**
     * 无效注解方法缓存用于快速返回
     */
    private final Set<String> invalidCacheSet = new ConcurrentHashSet<>();


    public Expression getSqlSegment(Expression where, String mappedStatementId, boolean isSelect) {
        Map<String, String> alias = findAnnotation(mappedStatementId);
        if (ArrayUtil.isEmpty(alias)) {
            invalidCacheSet.add(mappedStatementId);
            return where;
        }

        LoginUser loginUser = LoginHelper.getLoginUser();
        if (loginUser.getUserId() == 1L) {
            return where;
        }
        String dataFilterSql = buildDataFilterSql(alias, isSelect);
        if (StringUtils.isBlank(dataFilterSql)) {
            return where;
        }
        try {
            Expression expression = CCJSqlParserUtil.parseExpression(dataFilterSql);
            // 数据权限使用单独的括号 防止与其他条件冲突
            Parenthesis parenthesis = new Parenthesis(expression);
            if (ObjectUtil.isNotNull(where)) {
                return new AndExpression(where, parenthesis);
            } else {
                return parenthesis;
            }
        } catch (JSQLParserException ignored) {
            ignored.printStackTrace();
        }
        return where;
    }

    private String buildDataFilterSql(Map<String, String> alias, boolean isSelect) {
        String joinStr = isSelect ? " OR " : " AND ";
        LoginUser loginUser = LoginHelper.getLoginUser();

        StringBuilder sb = new StringBuilder();
        List<RoleDTO> sysRoleList = loginUser.getRoles();

        Set<String> conditions = new HashSet<>();
        for (RoleDTO sysRole : sysRoleList) {
            String sysRoleDataScope = sysRole.getDataScope();
            if (DATA_SCOPE_ALL.equals(sysRoleDataScope)) {
                // 1-全部权限
                return "";
            } else if (DATA_SCOPE_CUSTOM.equals(sysRoleDataScope)) {
                SysRoleService sysRoleService = SpringUtils.getBean(SysRoleService.class);
                List<SysDept> list = sysRoleService.selectDeptByRoleId(sysRole.getRoleId());
                // 2-自定义数据权限 也就是直接在角色部门表 查询
                sb.append(String.format(" %s.dept_id in(%s)", alias.get("dept"), StreamUtils.join(list, d -> Convert.toStr(d.getDeptId()))));
            } else if (DATA_SCOPE_DEPT.equals(sysRoleDataScope)) {
                // 3-本部门数据权限
                sb.append(String.format(" %s.dept_id=%d", alias.get("dept"), loginUser.getDeptId()));
            } else if (DATA_SCOPE_DEPT_AND_CHILD.equals(sysRoleDataScope)) {
                DeptService sysDeptService = SpringUtils.getBean(DeptService.class);
                // 4-本部门及子部门数据权限
                List<Long> deptIdList = sysDeptService.deptByParent(loginUser.getDeptId());
                sb.append(String.format(" %s.dept_id in(%s)", alias.get("dept"), StreamUtils.join(deptIdList, Convert::toStr)));
            } else if (DATA_SCOPE_SELF.equals(sysRoleDataScope)) {
                // 5只能查看当前用户信息，不能查看此角色部门信息和当前用户下的部门信息
                if (StringUtils.isEmpty(alias.get("user"))) {
                    sb.append(" 1=0");
                } else {
                    sb.append(String.format(" %s.user_id=%d", alias.get("user"), loginUser.getUserId()));
                }
            }
            conditions.add(joinStr + sb);
        }
        if (CollUtil.isNotEmpty(conditions)) {
            String sql = StreamUtils.join(conditions, Function.identity(), "");
            return sql.substring(joinStr.length());
        }
        return "";
    }

    private Map<String, String> findAnnotation(String mappedStatementId) {
        StringBuilder sb = new StringBuilder(mappedStatementId);
        int index = sb.lastIndexOf(".");
        String clazzName = sb.substring(0, index);
        String methodName = sb.substring(index + 1, sb.length());
        Class<?> clazz = ClassUtil.loadClass(clazzName);
        List<Method> methods = Arrays.stream(ClassUtil.getDeclaredMethods(clazz))
                .filter(method -> method.getName().equals(methodName)).collect(Collectors.toList());
        DataScope dataScope;

        for (Method method : methods) {
            dataScope = dataScopeMap.get(mappedStatementId);
            if (ObjectUtil.isNotNull(dataScope)) {
                return getDataScopeValue(dataScope);
            }
            if (AnnotationUtil.hasAnnotation(method, DataScope.class)) {
                dataScope = AnnotationUtil.getAnnotation(method, DataScope.class);
                dataScopeMap.put(mappedStatementId, dataScope);
                return getDataScopeValue(dataScope);
            }
        }

        if (AnnotationUtil.hasAnnotation(clazz, DataScope.class)) {
            dataScope = AnnotationUtil.getAnnotation(clazz, DataScope.class);
            return getDataScopeValue(dataScope);
        }
        return null;
    }

    /**
     * 不含有数据权限注解
     */
    public boolean isInvalid(String mappedStatementId) {
        return invalidCacheSet.contains(mappedStatementId);
    }

    private Map<String, String> getDataScopeValue(DataScope dataScope) {
        Map<String, String> attributes = new HashMap<>();
        if (dataScope != null) {
            if (StringUtils.isNotBlank(dataScope.deptAlias())) {
                attributes.put("dept", dataScope.deptAlias());
            }
            if (StringUtils.isNotBlank(dataScope.userAlias())) {
                attributes.put("user", dataScope.userAlias());
            }
        }
        return attributes;
    }
}
