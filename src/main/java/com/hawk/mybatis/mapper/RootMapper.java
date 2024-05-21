package com.hawk.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-05-21 10:10
 */
public interface RootMapper<T> extends BaseMapper<T> {
    int insertBatch(@Param("dataList") List<T> dataList);
}
