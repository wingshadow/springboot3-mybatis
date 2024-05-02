package com.hawk.admin.controller.car;

import com.github.pagehelper.PageInfo;
import com.hawk.admin.orm.entity.BizCarInfo;
import com.hawk.admin.orm.service.BizCarInfoService;
import com.hawk.mybatis.common.web.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-05-02 14:17
 */
@RestController
@RequestMapping(value = "car")
public class CarController {

    @Resource
    private BizCarInfoService carInfoService;


    @GetMapping("/list")
    public R<List<BizCarInfo>> getList(BizCarInfo carInfo){
        return R.ok(carInfoService.getCarInfoList(carInfo));
    }

    @GetMapping("/page")
    public R<PageInfo<BizCarInfo>> getPage(BizCarInfo carInfo,Integer pageSize,Integer pageNum){
        return R.ok(carInfoService.getCarInfoPage(carInfo,pageSize,pageNum));
    }
}
