package com.hawk.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hawk.common.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-26 15:20
 */
@Getter
@Setter
@TableName("sys_role_user")
public class SysRoleUser extends BaseEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(type = IdType.INPUT)
    private Long roleId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;
}
