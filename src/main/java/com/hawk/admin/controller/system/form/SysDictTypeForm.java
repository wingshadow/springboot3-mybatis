package com.hawk.admin.controller.system.form;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hawk.common.annotation.ExcelDictFormat;
import com.hawk.common.convert.ExcelDictConvert;
import com.hawk.common.core.form.BasePageForm;
import com.hawk.common.entity.BaseDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 字典类型表 sys_dict_type
 *
 * @author Lion Li
 */

@Data
public class SysDictTypeForm extends BasePageForm {

    private String dictId;

    private String dictName;

    private String dictType;

    private String status;

    private String remark;

}
