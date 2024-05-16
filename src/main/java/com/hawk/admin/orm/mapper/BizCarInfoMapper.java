package com.hawk.admin.orm.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hawk.admin.orm.entity.BizCarInfo;
import com.hawk.mybatis.annotation.DataScope;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-05-01 08:38
 */
public interface BizCarInfoMapper extends BaseMapper<BizCarInfo> {

    List<BizCarInfo> getCarInfoList(BizCarInfo carInfo);

    List<BizCarInfo> getCarInfoPage(BizCarInfo carInfo);

    @DataScope(deptAlias = "d")
    Page<BizCarInfo> getCarInfoPage2(@Param(Constants.WRAPPER) Wrapper<BizCarInfo> queryWrapper,
                                     @Param("page") Page<BizCarInfo> page);
}
