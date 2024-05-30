package com.hawk.admin.controller.system.form;

import com.hawk.common.core.form.BasePageForm;
import lombok.Data;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-05-20 10:34
 */
@Data
public class SysUserForm extends BasePageForm {

    private String userAccount;

    private String userName;

    private String mobile;

    private String deptId;

    private String status;
}
