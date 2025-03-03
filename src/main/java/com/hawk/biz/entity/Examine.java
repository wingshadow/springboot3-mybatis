package com.hawk.biz.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hawk.framework.common.core.entity.BaseDataEntity;
import com.hawk.framework.common.core.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 【请填写功能名称】对象 t_examee
 *
 * @author hawk
 * @date 2025-02-28
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("t_examine")
public class Examine extends BaseDataEntity {

    private static final long serialVersionUID=1L;

    /**
     * 考试主键
     */
    @TableId(value = "examine_id")
    private Long examineId;
    /**
     * 用户主键
     */
    private Long userId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String gender;
    /**
     * 证件类型
     */
    private String cardType;
    /**
     * 证件号码
     */
    private String idCard;
    /**
     * 出生日期;yyyy-mm-dd
     */
    private String birthday;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 最高学历
     */
    private String education;
    /**
     * 通信地址
     */
    private String address;
    /**
     * 所属公司
     */
    private String company;
    /**
     * 初领日期
     */
    private Date firstLicenseDate;
    /**
     * 准驾车型
     */
    private String vehicleType;

}
