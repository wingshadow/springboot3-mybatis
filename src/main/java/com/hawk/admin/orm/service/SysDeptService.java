package com.hawk.admin.orm.service;

import com.hawk.admin.orm.dao.SysDeptMapper;
import com.hawk.admin.orm.entity.SysDept;
import com.hawk.mybatis.common.database.BaseService;

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