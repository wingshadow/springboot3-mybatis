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
 * @create: 2024-04-26 15:14
 */
@Getter
@Setter
@TableName("sys_role")
public class SysRole extends BaseEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "role_id")
    private Long roleId;

    private String roleName;

    private String dataScope;
}
