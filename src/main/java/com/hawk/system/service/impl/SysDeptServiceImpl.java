package com.hawk.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.hawk.framework.common.constant.CacheNames;
import com.hawk.framework.common.constant.UserConstants;
import com.hawk.system.entity.SysRole;
import com.hawk.system.entity.SysUser;
import com.hawk.system.vo.SysDeptVO;
import com.hawk.common.exception.ServiceException;
import com.hawk.common.utils.CacheUtils;
import com.hawk.framework.helper.LoginHelper;
import com.hawk.system.mapper.SysDeptMapper;
import com.hawk.system.entity.SysDept;
import com.hawk.system.mapper.SysRoleMapper;
import com.hawk.system.mapper.SysUserMapper;
import com.hawk.system.service.SysDeptService;
import com.hawk.framework.helper.DataBaseHelper;
import com.hawk.framework.service.DeptService;
import com.hawk.mybatis.common.impl.BaseServiceImpl;
import com.hawk.common.utils.SpringUtils;
import com.hawk.utils.StreamUtils;
import com.hawk.utils.StringUtils;
import com.hawk.utils.TreeBuildUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private SysRoleMapper roleMapper;

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
        // 结果返回deptName的sql，例如：select dept_name from dept
        SysDept parentDept = baseMapper.selectOne(new LambdaQueryWrapper<SysDept>()
                .select(SysDept::getDeptName).eq(SysDept::getDeptId, dept.getParentId()));
        dept.setParentName(ObjectUtil.isNotNull(parentDept) ? parentDept.getDeptName() : null);
        return dept;
    }

    @Override
    public List<Long> deptByParent(Long parentDeptId) {
        List<Long> deptList = CollUtil.newArrayList(parentDeptId);
        LambdaQueryWrapper<SysDept> lqw = new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getParentId, parentDeptId).eq(SysDept::getIsDeleted, "0");
        deptList.addAll(CollUtil.emptyIfNull(baseMapper.selectList(lqw)).stream().map(SysDept::getDeptId)
                .toList());
        return deptList;
    }

    @Override
    public List<Long> deptByAncestors(String ancestors) {
        LambdaQueryWrapper<SysDept> lqw = new LambdaQueryWrapper<SysDept>()
                .likeLeft(SysDept::getAncestors, ancestors).eq(SysDept::getIsDeleted, "0");
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

    @Override
    public List<SysDept> getAllDeptList(SysDept sysDept) {
        return baseMapper.getAllDeptList();
    }


    public String getDeptAndChild(Long deptId) {
        List<SysDept> deptList = baseMapper.selectList(new LambdaQueryWrapper<SysDept>()
                .select(SysDept::getDeptId)
                .apply(DataBaseHelper.findInSet(deptId, "ancestors")));
        List<Long> ids = StreamUtils.toList(deptList, SysDept::getDeptId);
        ids.add(deptId);
        List<SysDept> list = baseMapper.selectList(new LambdaQueryWrapper<SysDept>()
                .select(SysDept::getDeptId)
                .in(SysDept::getDeptId, ids));
        if (CollUtil.isNotEmpty(list)) {
            return StreamUtils.join(list, d -> Convert.toStr(d.getDeptId()));
        }
        return null;
    }

    @Override
    public List<SysDept> selectDeptList(SysDept dept) {
        LambdaQueryWrapper<SysDept> lqw = new LambdaQueryWrapper<>();
        lqw.eq(SysDept::getIsDeleted, UserConstants.DEPT_NORMAL)
                .eq(ObjectUtil.isNotNull(dept.getDeptId()), SysDept::getDeptId, dept.getDeptId())
                .eq(ObjectUtil.isNotNull(dept.getParentId()), SysDept::getParentId, dept.getParentId())
                .like(StringUtils.isNotBlank(dept.getDeptName()), SysDept::getDeptName, dept.getDeptName())
                .eq(ObjectUtil.isNotEmpty(dept.getStatus()), SysDept::getStatus, dept.getStatus())
                .orderByAsc(SysDept::getParentId)
                .orderByAsc(SysDept::getOrderNum);
        return baseMapper.selectDeptList(lqw);
    }

    @Override
    public List<Tree<String>> selectDeptTreeList(SysDept dept) {
        List<SysDept> depts = this.selectDeptList(dept);

        return buildDeptTreeSelect(depts);
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    @Override
    public List<Tree<String>> buildDeptTreeSelect(List<SysDept> depts) {
        if (CollUtil.isEmpty(depts)) {
            return CollUtil.newArrayList();
        }
        // 解决ID超过16位精度不足的问题
        List<SysDeptVO> list = BeanUtil.copyToList(depts, SysDeptVO.class);

        return TreeBuildUtils.build(list, (deptVO, tree) ->
                tree.setId(deptVO.getDeptId())
                        .setParentId(deptVO.getParentId())
                        .setName(deptVO.getDeptName())
                        .setWeight(deptVO.getOrderNum()));
    }

    @Override
    public void checkDeptDataScope(Long deptId) {
        if (!LoginHelper.isAdmin()) {
            SysDept dept = new SysDept();
            dept.setDeptId(deptId);
            List<SysDept> depts = this.selectDeptList(dept);
            if (CollUtil.isEmpty(depts)) {
                throw new ServiceException("没有权限访问部门数据！");
            }
        }
    }

    @Override
    public boolean checkDeptNameUnique(SysDept dept) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getDeptName, dept.getDeptName())
                .eq(SysDept::getParentId, dept.getParentId())
                .ne(ObjectUtil.isNotNull(dept.getDeptId()), SysDept::getDeptId, dept.getDeptId()));
        return !exist;
    }

    @Override
    public int insertDept(SysDept dept) {
        SysDept info = baseMapper.selectById(dept.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus().toString())) {
            throw new ServiceException("部门停用，不允许新增");
        }
        dept.setAncestors(info.getAncestors() + StringUtils.SEPARATOR + dept.getParentId());
        return baseMapper.insert(dept);
    }

    @Override
    public long selectNormalChildrenDeptById(Long deptId) {
        return baseMapper.selectCount(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getStatus, UserConstants.DEPT_NORMAL)
                .apply(DataBaseHelper.findInSet(deptId, "ancestors")));
    }

    @Override
    public int updateDept(SysDept dept) {
        SysDept newParentDept = baseMapper.selectById(dept.getParentId());
        SysDept oldDept = baseMapper.selectById(dept.getDeptId());
        if (ObjectUtil.isNotNull(newParentDept) && ObjectUtil.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + StringUtils.SEPARATOR + newParentDept.getDeptId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
        }
        int result = baseMapper.updateById(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus().toString()) && StringUtils.isNotEmpty(dept.getAncestors())
                && !StringUtils.equals(UserConstants.DEPT_NORMAL, dept.getAncestors())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
        return result;
    }

    private void updateParentDeptStatusNormal(SysDept dept) {
        String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        baseMapper.update(null, new LambdaUpdateWrapper<SysDept>()
                .set(SysDept::getStatus, UserConstants.DEPT_NORMAL)
                .in(SysDept::getDeptId, Arrays.asList(deptIds)));
    }

    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {
        List<SysDept> children = baseMapper.selectList(new LambdaQueryWrapper<SysDept>()
                .apply(DataBaseHelper.findInSet(deptId, "ancestors")));
        List<SysDept> list = new ArrayList<>();
        for (SysDept child : children) {
            SysDept dept = new SysDept();
            dept.setDeptId(child.getDeptId());
            dept.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
            list.add(dept);
        }
        if (CollUtil.isNotEmpty(list)) {
            if (baseMapper.updateBatchById(list)) {
                list.forEach(dept -> CacheUtils.evict(CacheNames.SYS_DEPT, dept.getDeptId()));
            }
        }
    }

    @Override
    public boolean hasChildByDeptId(Long deptId) {
        return baseMapper.exists(new LambdaQueryWrapper<SysDept>()
                .eq(SysDept::getParentId, deptId));
    }

    @Override
    public boolean checkDeptExistUser(Long deptId) {
        return userMapper.exists(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getDeptId, deptId));
    }

    @Override
    public List<Long> selectDeptListByRoleId(Long roleId){
        SysRole role = roleMapper.selectById(roleId);
        return baseMapper.selectDeptListByRoleId(roleId, role.getDeptCheckStrictly());
    }
}
