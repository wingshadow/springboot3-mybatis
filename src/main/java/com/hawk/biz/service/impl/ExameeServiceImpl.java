package com.hawk.biz.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hawk.biz.entity.Examine;
import com.hawk.biz.mapper.ExamineMapper;
import com.hawk.biz.service.ExamineService;
import com.hawk.framework.mybatis.common.impl.BaseServiceImpl;
import com.hawk.framework.web.page.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 【请填写功能名称】Service业务层处理
 *
 * @author hawk
 * @date 2025-02-28
 */
@RequiredArgsConstructor
@Service
public class ExameeServiceImpl extends BaseServiceImpl<ExamineMapper, Examine> implements ExamineService {

    private final ExamineMapper baseMapper;

    /**
     * 新增【请填写功能名称】
     */
    @Override
    public boolean insert(Examine paramBean) {
        return super.insert(paramBean);
    }


    /**
     * 删除【请填写功能名称】
     */
    @Override
    public boolean deleteByPrimaryKey(Long id) {
        return super.deleteByPrimaryKey(id);
    }

    /**
     * 批量删除【请填写功能名称】
     */
    @Override
    public void deleteBatchByPrimaryKeys(String ids){
        super.deleteBatchByPrimaryKeys(ids);
    }


    /**
     * 批量删除【请填写功能名称】
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteBatchByPrimaryKeys(List<Long> ids) {
        ids.forEach(this::deleteByPrimaryKey);
    }

    /**
     * 修改【请填写功能名称】
     */
    @Override
    public boolean updateByPrimaryKeySelective(Examine paramBean) {
        return super.updateByPrimaryKeySelective(paramBean);
    }

    /**
     * 查询【请填写功能名称】
     */
    @Override
    public Examine getByPrimaryKey(Long id) {
        return super.getByPrimaryKey(id);
    }

    /**
     * 查询【请填写功能名称】
     */
    @Override
    public List<Examine> listByConditions(Examine paramBean) {
        return super.listByConditions(paramBean);
    }

    /**
     * 查询【请填写功能名称】
     */
    @Override
    public List<Examine> listAll() {
        return super.listAll();
    }

    /**
     * 查询【请填写功能名称】
     */
    @Override
    public Examine listOne(Examine paramBean) {
        return super.listOne(paramBean);
    }

    /**
     * 查询【请填写功能名称】列表
     */
    @Override
    public PageInfo<Examine> selectPageList(Examine paramBean, int pageSize, int pageNum){
        LambdaQueryWrapper<Examine> lqw = buildQueryWrapper(paramBean);
        Page<Examine> page = new Page<>(pageNum, pageSize);
        page = baseMapper.selectPage(page, lqw);
        return PageInfo.build(page);
    }


    private LambdaQueryWrapper<Examine> buildQueryWrapper(Examine paramBean) {
        return new LambdaQueryWrapper<>();
        /*Map<String, Object> params = paramBean.getParams();
                LambdaQueryWrapper<Examee> lqw = Wrappers.lambdaQuery();
                lqw.eq(paramBean.getUserId() != null, Examee::getUserId, bo.getUserId());
                lqw.like(StringUtils.isNotBlank(paramBean.getName()), Examee::getName, bo.getName());
                lqw.eq(StringUtils.isNotBlank(paramBean.getGender()), Examee::getGender, bo.getGender());
                lqw.eq(StringUtils.isNotBlank(paramBean.getCardType()), Examee::getCardType, bo.getCardType());
                lqw.eq(StringUtils.isNotBlank(paramBean.getIdCard()), Examee::getIdCard, bo.getIdCard());
                lqw.eq(StringUtils.isNotBlank(paramBean.getBrithday()), Examee::getBrithday, bo.getBrithday());
                lqw.eq(StringUtils.isNotBlank(paramBean.getEducation()), Examee::getEducation, bo.getEducation());
                lqw.eq(StringUtils.isNotBlank(paramBean.getAdderss()), Examee::getAdderss, bo.getAdderss());
                lqw.eq(StringUtils.isNotBlank(paramBean.getCompany()), Examee::getCompany, bo.getCompany());
                lqw.eq(paramBean.getFirstLicenseDate() != null, Examee::getFirstLicenseDate, bo.getFirstLicenseDate());
                lqw.eq(StringUtils.isNotBlank(paramBean.getVehicleType()), Examee::getVehicleType, bo.getVehicleType());
                return lqw;*/
    }

}
