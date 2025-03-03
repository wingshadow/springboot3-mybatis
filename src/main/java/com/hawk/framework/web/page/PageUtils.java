package com.hawk.framework.web.page;

import cn.hutool.core.bean.BeanUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2025-02-27 15:37
 */
public class PageUtils {

    public static <E, T> PageInfo<T> convert(PageInfo<E>  result, Class<T> targetClass) {
        PageInfo<T> pageInfo = new PageInfo<>();
        pageInfo.setTotal(result.getTotal());
        pageInfo.setMsg(result.getMsg());
        pageInfo.setCode(result.getCode());

        // 创建转换后的 list
        List<T> list = new ArrayList<>();
        // 遍历 result 的 list，使用 BeanUtil.copyProperties 转换为目标类型
        for (E r : result.getList()) {
            T target = BeanUtil.copyProperties(r, targetClass); // 将源对象转换为目标类型
            list.add(target);
        }

        pageInfo.setList(list);
        return pageInfo;

    }
}
