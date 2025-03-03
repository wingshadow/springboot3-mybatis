package com.hawk.framework.mybatis.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hawk.framework.common.core.entity.BaseEntity;
import com.hawk.framework.web.page.PageInfo;
import com.hawk.system.entity.SysConfig;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2023-02-14 14:05
 */
public interface BaseService<M extends BaseMapper<T>, T extends BaseEntity> extends IService<T> {


    boolean insert(T paramBean);

    /**
     * 根据主键ID，删除一条SysDept记录
     *
     * @param id SysDept的主键
     * @return
     */
    boolean deleteByPrimaryKey(Long id);

    /**
     * 根据主键ID，批量删除多条SysDept记录
     *
     * @param ids SysDept的主键字符串，多个主键用英文逗号分隔
     */
    void deleteBatchByPrimaryKeys(String ids);

    /**
     * 根据主键ID，批量删除多条SysDept记录
     *
     * @param ids SysDept的主键集合
     */
    void deleteBatchByPrimaryKeys(List<Long> ids);

    /**
     * 根据主键更新SysDept数据记录
     *
     * @param paramBean 要更新的SysDept数据对象
     */
    boolean updateByPrimaryKeySelective(T paramBean);

    /**
     * 根据主键查询SysDept数据对象
     *
     * @param id SysDept的主键
     * @return SysDept数据对象
     */
    T getByPrimaryKey(Long id);

    /**
     * 查询符合条件的SysDept结果集,根据paramBean动态拼接查询条件。
     *
     * @param paramBean 用于封装查询条件
     * @return SysDept数据查询结果集
     */
    List<T> listByConditions(T paramBean);

    /**
     * @return
     */
    List<T> listAll();

    /**
     * @param paramBean
     * @return
     */
    T listOne(T paramBean);

    PageInfo<T> selectPageList(T paramBean, int pageSize, int pageNum);
}
