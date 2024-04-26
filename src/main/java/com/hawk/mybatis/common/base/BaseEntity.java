package com.hawk.mybatis.common.base;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-04-25 15:04
 */
@Data
public class BaseEntity {
    @TableField(exist = false)
    private Map<String,String> params=new HashMap<>();

}
