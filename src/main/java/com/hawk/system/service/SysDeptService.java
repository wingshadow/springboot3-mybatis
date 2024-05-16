package com.hawk.system.service;

import com.hawk.system.mapper.SysDeptMapper;
import com.hawk.common.core.domain.entity.SysDept;
import com.hawk.mybatis.common.BaseService;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2023-02-14 14:58
 */
public interface SysDeptService extends BaseService<SysDeptMapper, SysDept> {

    List<SysDept> getAllDeptList(SysDept sysDept);
    String getDeptAndChild(Long deptId);

}