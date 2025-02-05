package com.hawk.generator.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.hawk.framework.mybatis.mapper.BaseMapperPlus;
import com.hawk.generator.domain.GenTableColumn;

import java.util.List;

/**
 * 业务字段 数据层
 *
 * @author Lion Li
 */
@InterceptorIgnore(dataPermission = "true")
public interface GenTableColumnMapper extends BaseMapperPlus<GenTableColumn> {
    /**
     * 根据表名称查询列信息
     *
     * @param tableName 表名称
     * @return 列信息
     */
    List<GenTableColumn> selectDbTableColumnsByName(String tableName);

}
