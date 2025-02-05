package com.hawk.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-05-30 11:40
 */
@Data
public class SysDeptVO {

    private String deptId;

    private String parentId;

    private String deptName;

    private Integer orderNum;

}
