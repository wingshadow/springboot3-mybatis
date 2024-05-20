package com.hawk.common.core.form;

import cn.hutool.core.util.ObjectUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-05-20 10:33
 */
@Data
public class BasePageForm extends BaseForm {

    private Integer pageSize;

    private Integer pageNum;

    public Integer getPageSize() {
        return ObjectUtil.isEmpty(pageSize) ? 10 : pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return ObjectUtil.isEmpty(pageNum) ? 1 : pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }
}
