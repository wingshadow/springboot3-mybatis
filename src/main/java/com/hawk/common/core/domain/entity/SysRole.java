package com.hawk.common.core.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hawk.common.constant.UserConstants;
import com.hawk.common.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-26 15:14
 */
@Getter
@Setter
@NoArgsConstructor
@TableName("sys_role")
public class SysRole extends BaseEntity {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId(value = "role_id")
    private Long roleId;

    private String roleName;

    private String dataScope;

    /**
     * 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示）
     */
    private Boolean menuCheckStrictly;

    /**
     * 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ）
     */
    private Boolean deptCheckStrictly;

    private Integer sort;

    private Integer status;

    @TableField(exist = false)
    private Long[] menuIds;

    @TableField(exist = false)
    private Long[] deptIds;

    public SysRole(Long roleId){
        this.roleId = roleId;
    }
    public boolean isAdmin() {
        return UserConstants.ADMIN_ID.equals(this.roleId);
    }
}
