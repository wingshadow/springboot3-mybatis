package com.hawk.common.web.page;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.http.HttpStatus;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hawk.utils.DefUtil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-05-05 14:56
 */
@Data
@NoArgsConstructor
public class PageInfo<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 列表数据
     */
    private List<T> rows;

    /**
     * 消息状态码
     */
    private int code;

    /**
     * 消息内容
     */
    private String msg;

    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    public PageInfo(List<T> list, long total) {
        this.rows = list;
        this.total = total;
    }

    public static <T> PageInfo<T> build(IPage<T> page) {
        PageInfo<T> pageInfo = new PageInfo<>();
        pageInfo.setCode(HttpStatus.HTTP_OK);
        pageInfo.setMsg("查询成功");
        pageInfo.setRows(page.getRecords());
        pageInfo.setTotal(page.getTotal());
        return pageInfo;
    }

    public static <T> PageInfo<T> build(List<T> list) {
        PageInfo<T> pageInfo = new PageInfo<>();
        pageInfo.setCode(HttpStatus.HTTP_OK);
        pageInfo.setMsg("查询成功");
        pageInfo.setRows(list);
        pageInfo.setTotal(list.size());
        return pageInfo;
    }

    public static <T> PageInfo<T> build() {
        PageInfo<T> pageInfo = new PageInfo<>();
        pageInfo.setCode(HttpStatus.HTTP_OK);
        pageInfo.setMsg("查询成功");
        return pageInfo;
    }

    public static <R, T> PageInfo<R> build(PageInfo<T> tableDataInfo, Class<R> type) {
        return build(tableDataInfo, t -> BeanUtil.toBean(t, type));
    }

    public static <R, T> PageInfo<R> build(PageInfo<T> tableDataInfo, Function<T, R> function) {
        PageInfo<R> pageInfo = new PageInfo<>();
        pageInfo.setCode(tableDataInfo.getCode());
        pageInfo.setMsg(tableDataInfo.getMsg());
        pageInfo.setRows(DefUtil.def(tableDataInfo.getRows()).stream().map(function).collect(Collectors.toList()));
        pageInfo.setTotal(tableDataInfo.getTotal());
        return pageInfo;
    }

    public static <R, T> PageInfo<R> build(IPage<T> page, Function<T, R> function) {
        IPage<R> result = new Page<>();
        result.setPages(page.getPages());
        result.setSize(page.getSize());
        result.setCurrent(page.getCurrent());
        result.setTotal(page.getTotal());
        result.setRecords(page.getRecords().stream().map(function).collect(Collectors.toList()));
        return PageInfo.build(result);
    }
}
