package com.hawk.system.service;

import cn.hutool.core.lang.tree.Tree;
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

    List<SysDept> selectDeptList(SysDept dept);

    List<Tree<Long>> selectDeptTreeList(SysDept dept);

    List<Tree<Long>> buildDeptTreeSelect(List<SysDept> depts);

    void checkDeptDataScope(Long deptId);

    boolean checkDeptNameUnique(SysDept dept);

    int insertDept(SysDept dept);

    long selectNormalChildrenDeptById(Long deptId);

    int updateDept(SysDept dept);

    boolean hasChildByDeptId(Long deptId);

    boolean checkDeptExistUser(Long deptId);

    List<Long> selectDeptListByRoleId(Long roleId);

}