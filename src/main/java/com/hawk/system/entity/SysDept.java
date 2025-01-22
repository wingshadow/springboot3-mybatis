package com.hawk.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hawk.framework.common.core.entity.TreeEntity;
import lombok.Getter;
import lombok.Setter;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-26 15:04
 */
@Getter
@Setter
@TableName("sys_dept")
public class SysDept extends TreeEntity<SysDept> {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "dept_id")
    private Long deptId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;

    private String deptName;

    private String ancestors;

    private Integer orderNum;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String isDeleted;
}
