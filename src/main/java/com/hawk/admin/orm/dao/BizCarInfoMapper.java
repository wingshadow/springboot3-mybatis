package com.hawk.admin.orm.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hawk.admin.orm.entity.BizCarInfo;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-05-01 08:38
 */
public interface BizCarInfoMapper extends BaseMapper<BizCarInfo> {

    List<BizCarInfo> getCarInfoList(BizCarInfo carInfo);
}
