package com.hawk.admin.orm.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hawk.mybatis.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-05-01 08:36
 */
@Getter
@Setter
@TableName("biz_car_info")
public class BizCarInfo extends BaseEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "car_id")
    private Long carId;

    private Long deptId;

    private String deptName;

    private String carNum;

}
