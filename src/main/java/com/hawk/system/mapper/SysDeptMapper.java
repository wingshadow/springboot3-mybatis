package com.hawk.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.hawk.framework.annotation.scope.DataScope;
import com.hawk.framework.mybatis.mapper.BaseMapperPlus;
import com.hawk.system.entity.SysDept;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-26 15:09
 */
public interface SysDeptMapper extends BaseMapperPlus<SysDept> {

    List<SysDept> getAllDeptList();

    @DataScope(deptAlias = "d")
    Page<SysDept> selectPageDeptList(@Param(Constants.WRAPPER) Wrapper<SysDept> queryWrapper,
                                 @Param("page") Page<SysDept> page);

    @DataScope(deptAlias = "d")
    @Select("select * from sys_dept d ${ew.getCustomSqlSegment}")
    List<SysDept> selectDeptList(@Param(Constants.WRAPPER) Wrapper<SysDept> queryWrapper);

    List<Long> selectDeptListByRoleId(@Param("roleId") Long roleId, @Param("deptCheckStrictly") boolean deptCheckStrictly);
}
