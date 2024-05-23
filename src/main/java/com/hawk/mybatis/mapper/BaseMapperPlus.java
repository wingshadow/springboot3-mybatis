package com.hawk.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.toolkit.Db;

import java.util.Collection;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-05-21 14:21
 */
public interface BaseMapperPlus<T> extends BaseMapper<T> {

    default boolean insertBatch(Collection<T> entityList) {
        return Db.saveBatch(entityList);
    }

    /**
     * 批量更新
     */
    default boolean updateBatchById(Collection<T> entityList) {
        return Db.updateBatchById(entityList);
    }

    default boolean insertOrUpdateBatch(Collection<T> entityList) {
        return Db.saveOrUpdateBatch(entityList);
    }
}
