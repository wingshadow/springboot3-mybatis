package com.hawk.admin.orm.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hawk.mybatis.common.base.BaseEntity;
import lombok.*;

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
public class SysUser extends BaseEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "user_id")
    private Long userId;

    private String userName;

    private String account;

    private String password;

    private Long deptId;

    private Integer delFlag;

    @JsonIgnore
    @TableField(exist = false)
    private List<SysRole> sysRoleList;
}