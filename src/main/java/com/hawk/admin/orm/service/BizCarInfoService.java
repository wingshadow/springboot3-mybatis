package com.hawk.admin.orm.service;

import com.hawk.admin.orm.dao.BizCarInfoMapper;
import com.hawk.admin.orm.entity.BizCarInfo;
import com.hawk.mybatis.common.database.BaseService;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-05-01 08:40
 */
public interface BizCarInfoService extends BaseService<BizCarInfoMapper, BizCarInfo> {

    List<BizCarInfo> getCarInfoList(BizCarInfo params);
}
