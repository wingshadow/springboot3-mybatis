package com.hawk.admin.orm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hawk.admin.orm.dao.BizCarInfoMapper;
import com.hawk.admin.orm.entity.BizCarInfo;
import com.hawk.admin.orm.service.BizCarInfoService;
import com.hawk.mybatis.annotation.DataScope;
import com.hawk.mybatis.common.database.impl.BaseServiceImpl;
import com.hawk.mybatis.page.PageInfo;
import com.hawk.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-05-01 08:40
 */
@Service
public class BizCarInfoServiceImpl extends BaseServiceImpl<BizCarInfoMapper, BizCarInfo> implements BizCarInfoService {

    @DataScope(deptAlias = "d")
    public List<BizCarInfo> getCarInfoList(BizCarInfo carInfo) {
        return baseMapper.getCarInfoList(carInfo);
    }


    @DataScope(deptAlias = "d")
    public List<BizCarInfo> getCarInfoPage(BizCarInfo carInfo, int pageSize, int pageNum) {
        return baseMapper.getCarInfoPage(carInfo);
    }


    public PageInfo<BizCarInfo> getCarInfoPage2(BizCarInfo bizCarInfo, int pageSize, int pageNum) {
        Page<BizCarInfo> page = new Page<>(pageNum,pageSize);
        QueryWrapper<BizCarInfo> query = Wrappers.query();
        query.eq(StringUtils.isNotBlank(bizCarInfo.getCarNum()),"car_num",bizCarInfo.getCarNum());
        page = baseMapper.getCarInfoPage2(query,page);
        return new PageInfo<>(page.getRecords(), (int) page.getPages());
    }

}
