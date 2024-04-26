package com.hawk.admin.orm.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hawk.mybatis.common.base.BaseEntity;
import lombok.*;

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

    @TableId(value = "user_id")
    private Long userId;

    private String user_name;

    private Long dept_id;

    private Short delFlag;
}