package com.hawk.framework.annotation.common;

import java.lang.annotation.*;

/**
 * 表单字段与业务类字段关联
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface VarRelation {

    String value();
}
