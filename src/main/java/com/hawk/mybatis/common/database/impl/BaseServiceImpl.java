package com.hawk.mybatis.common.database.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hawk.mybatis.common.base.BaseEntity;
import com.hawk.mybatis.common.database.BaseService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2023-02-14 14:36
 */
public abstract class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements BaseService<M, T> {

    public boolean insert(T paramBean) {
        return baseMapper.insert(paramBean) > 0;
    }

    @Override
    public boolean deleteByPrimaryKey(Long id) {
        return baseMapper.deleteById(id) > 0;
    }

    @Override
    public void deleteBatchByPrimaryKeys(String ids) {
        String[] stringList = StringUtils.split(ids, ",");
        assert stringList != null;
        List<Long> idLst = Arrays.stream(stringList).map(Long::parseLong).collect(Collectors.toList());
        deleteBatchByPrimaryKeys(idLst);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void deleteBatchByPrimaryKeys(List<Long> ids) {
        ids.forEach(this::deleteByPrimaryKey);
    }

    @Override
    public boolean updateByPrimaryKeySelective(T paramBean) {
        return baseMapper.updateById(paramBean) > 0;
    }

    @Override
    public T getByPrimaryKey(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public List<T> listByConditions(T paramBean) {
        return baseMapper.selectList(new LambdaQueryWrapper<>(paramBean));
    }

    @Override
    public List<T> listAll() {
        return baseMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public T listOne(T paramBean) {
        return baseMapper.selectOne(new LambdaQueryWrapper<>(paramBean));
    }


}