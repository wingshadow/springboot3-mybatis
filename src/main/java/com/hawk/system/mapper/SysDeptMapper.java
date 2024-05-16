package com.hawk.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hawk.common.core.domain.entity.SysDept;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-26 15:09
 */
public interface SysDeptMapper extends BaseMapper<SysDept> {

    List<SysDept> getAllDeptList();
}
