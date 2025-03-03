package com.hawk.controller.biz.examine.form;

import com.hawk.framework.common.core.entity.BaseEntity;
import com.hawk.framework.common.core.form.BasePageForm;
import com.hawk.framework.validate.AddGroup;
import com.hawk.framework.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.util.Date;

/**
 * 【请填写功能名称】业务对象 t_examee
 *
 * @author hawk
 * @date 2025-02-28
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class ExamineForm extends BasePageForm {

    /**
     * 考试主键
     */
    @NotNull(message = "考试主键不能为空", groups = { EditGroup.class })
    private Long examineId;

    /**
     * 用户主键
     */
    @NotNull(message = "用户主键不能为空", groups = { AddGroup.class, EditGroup.class })
    private Long userId;

    /**
     * 姓名
     */
    @NotBlank(message = "姓名不能为空", groups = { AddGroup.class, EditGroup.class })
    private String name;

    /**
     * 性别
     */
    @NotBlank(message = "性别不能为空", groups = { AddGroup.class, EditGroup.class })
    private String gender;

    /**
     * 证件类型
     */
    @NotBlank(message = "证件类型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String cardType;

    /**
     * 证件号码
     */
    @NotBlank(message = "证件号码不能为空", groups = { AddGroup.class, EditGroup.class })
    private String idCard;

    /**
     * 出生日期;yyyy-mm-dd
     */
    @NotBlank(message = "出生日期;yyyy-mm-dd不能为空", groups = { AddGroup.class, EditGroup.class })
    private String birthday;

    /**
     * 最高学历
     */
    @NotBlank(message = "最高学历不能为空", groups = { AddGroup.class, EditGroup.class })
    private String education;

    /**
     * 通信地址
     */
    @NotBlank(message = "通信地址不能为空", groups = { AddGroup.class, EditGroup.class })
    private String adderss;

    /**
     * 所属公司
     */
    @NotBlank(message = "所属公司不能为空", groups = { AddGroup.class, EditGroup.class })
    private String company;

    /**
     * 初领日期
     */
    @NotNull(message = "初领日期不能为空", groups = { AddGroup.class, EditGroup.class })
    private Date firstLicenseDate;

    /**
     * 准驾车型
     */
    @NotBlank(message = "准驾车型不能为空", groups = { AddGroup.class, EditGroup.class })
    private String vehicleType;


}
