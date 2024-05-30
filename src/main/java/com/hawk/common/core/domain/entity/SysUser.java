package com.hawk.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hawk.common.annotation.Sensitive;
import com.hawk.common.constant.UserConstants;
import com.hawk.common.entity.BaseDataEntity;
import com.hawk.common.enums.SensitiveStrategy;
import com.hawk.common.xss.Xss;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2023-02-14 14:52
 */

@Getter
@Setter
@TableName("sys_user")
public class SysUser extends BaseDataEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "user_id")
    private Long userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long deptId;

    /**
     * 用户账户
     */
    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 30, message = "用户账号长度不能超过{max}个字符")
    private String userAccount;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户姓名
     */
    @Xss(message = "用户昵称不能包含脚本字符")
    @Size(min = 0, max = 30, message = "用户昵称长度不能超过{max}个字符")
    private String userName;

    private String nickName;

    private Integer gender;

    @Sensitive(strategy = SensitiveStrategy.PHONE)
    private String mobile;

    @Sensitive(strategy = SensitiveStrategy.EMAIL)
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过{max}个字符")
    private String email;

    private String avatar;

    private String userType;

    private Integer status;

    private Integer delFlag;

    @JsonIgnore
    @TableField(exist = false)
    private List<SysRole> sysRoleList;

    @TableField(exist = false)
    private SysDept dept;

    @TableField(exist = false)
    private List<SysRole> roles;

    @TableField(exist = false)
    private Long roleId;
    public boolean isAdmin() {
        return UserConstants.ADMIN_ID.equals(this.userId);
    }
}