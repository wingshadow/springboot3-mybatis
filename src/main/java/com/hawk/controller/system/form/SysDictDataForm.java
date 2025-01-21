package com.hawk.controller.system.form;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.hawk.common.annotation.ExcelDictFormat;
import com.hawk.common.convert.ExcelDictConvert;
import com.hawk.common.core.form.BasePageForm;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-05-29 09:09
 */
@Data
public class SysDictDataForm extends BasePageForm {
    private String dictCode;

    private String dictSort;

    private String dictLabel;

    private String dictValue;

    private String dictType;

    private String cssClass;

    private String listClass;

    private String isDefault;

    private String status;

    private String remark;
}
