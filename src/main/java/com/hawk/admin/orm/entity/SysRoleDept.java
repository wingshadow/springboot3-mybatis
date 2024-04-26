package com.hawk.admin.orm.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hawk.mybatis.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-26 15:19
 */
@Getter
@Setter
@TableName("sys_role_dept")
public class SysRoleDept extends BaseEntity {

    @TableId(type = IdType.INPUT)
    private Long roleId;

    private Long deptId;
}
