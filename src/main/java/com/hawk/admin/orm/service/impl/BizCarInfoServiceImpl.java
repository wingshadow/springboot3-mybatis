package com.hawk.admin.orm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.hawk.admin.orm.dao.BizCarInfoMapper;
import com.hawk.admin.orm.entity.BizCarInfo;
import com.hawk.admin.orm.service.BizCarInfoService;
import com.hawk.mybatis.annotation.DataScope;
import com.hawk.mybatis.common.database.impl.BaseServiceImpl;
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
    public List<BizCarInfo> getCarInfoList(BizCarInfo carInfo){
        return baseMapper.getCarInfoList(carInfo);
    }


    @DataScope(deptAlias = "d")
    public PageInfo<BizCarInfo> getCarInfoPage(BizCarInfo carInfo,int pageSize,int pageNum){
        PageHelper.startPage(pageNum, pageSize);
        List<BizCarInfo> list =  baseMapper.getCarInfoList(carInfo);
        return new PageInfo<>(list);
    }

}
