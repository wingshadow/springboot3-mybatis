package com.hawk.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hawk.common.core.domain.entity.SysDept;
import com.hawk.common.core.domain.entity.SysUser;
import com.hawk.mybatis.annotation.DataScope;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-26 15:09
 */
public interface SysDeptMapper extends BaseMapper<SysDept> {

    List<SysDept> getAllDeptList();

    @DataScope(deptAlias = "d")
    Page<SysDept> selectDeptList(@Param(Constants.WRAPPER) Wrapper<SysDept> queryWrapper,
                                 @Param("page") Page<SysDept> page);

    @DataScope(deptAlias = "d")
    List<SysDept> selectDeptList(@Param(Constants.WRAPPER) Wrapper<SysDept> queryWrapper);
}
