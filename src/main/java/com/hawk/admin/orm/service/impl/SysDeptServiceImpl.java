package com.hawk.admin.orm.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hawk.admin.orm.dao.SysDeptMapper;
import com.hawk.admin.orm.entity.SysDept;
import com.hawk.admin.orm.service.SysDeptService;
import com.hawk.framework.service.DeptService;
import com.hawk.mybatis.common.database.impl.BaseServiceImpl;
import com.hawk.utils.SpringUtils;
import com.hawk.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-26 15:10
 */
@Service
public class SysDeptServiceImpl extends BaseServiceImpl<SysDeptMapper, SysDept> implements SysDeptService, DeptService {
    @Override
    public String selectDeptNameByIds(String deptIds) {
        List<String> list = new ArrayList<>();
        for (Long id : StringUtils.splitTo(deptIds, Convert::toLong)) {
            SysDept dept = SpringUtils.getAopProxy(this).selectDeptById(id);
            if (ObjectUtil.isNotNull(dept)) {
                list.add(dept.getDeptName());
            }
        }
        return String.join(StringUtils.SEPARATOR, list);
    }

    public SysDept selectDeptById(Long deptId) {
        SysDept dept = baseMapper.selectById(deptId);
        if (ObjectUtil.isNull(dept)) {
            return null;
        }
        SysDept parentDept = baseMapper.selectOne(new LambdaQueryWrapper<SysDept>()
                .select(SysDept::getDeptName).eq(SysDept::getDeptId, dept.getParentId()));
        dept.setParentName(ObjectUtil.isNotNull(parentDept) ? parentDept.getDeptName() : null);
        return dept;
    }

    @Override
    public List<Long> deptByParent(Long parentDeptId) {
        List<Long> deptList = CollUtil.newArrayList(parentDeptId);
        LambdaQueryWrapper<SysDept> lqw = new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getParentId, parentDeptId).eq(SysDept::getDelFlag, "0");
        deptList.addAll(CollUtil.emptyIfNull(baseMapper.selectList(lqw)).stream().map(SysDept::getDeptId)
                .collect(Collectors.toList()));
        return deptList;
    }

    @Override
    public List<Long> deptByAncestors(String ancestors) {
        LambdaQueryWrapper<SysDept> lqw = new LambdaQueryWrapper<SysDept>()
                .likeLeft(SysDept::getAncestors, ancestors).eq(SysDept::getDelFlag, "0");
        return CollUtil.emptyIfNull(baseMapper.selectList(lqw)).stream().map(SysDept::getDeptId)
                .collect(Collectors.toList());
    }

    @Override
    public SysDept deptById(Long deptId) {
        SysDept dept = baseMapper.selectById(deptId);
        if (ObjectUtil.isNull(dept)) {
            return null;
        }
        SysDept parentDept = baseMapper.selectOne(new LambdaQueryWrapper<SysDept>()
                .select(SysDept::getDeptName).eq(SysDept::getDeptId, dept.getParentId()));
        dept.setParentName(ObjectUtil.isNotNull(parentDept) ? parentDept.getDeptName() : null);
        return dept;
    }
}
