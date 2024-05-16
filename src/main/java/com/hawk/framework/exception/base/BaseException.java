package com.hawk.framework.exception.base;

import com.hawk.common.utils.MessageUtils;
import com.hawk.utils.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @program: springboot3-mybatis
 * @description:
 * @author: zhb
 * @create: 2024-05-06 14:39
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class BaseException extends RuntimeException {

    private String module;

    private String code;

    private Object[] args;

    private String message;

    public BaseException(String module, String code, Object[] args, String message) {
        this.module = module;
        this.code = code;
        this.args = args;
        this.message = message;
    }

    public BaseException(String module, String code, Object[] args) {
        this(module, code, args, null);
    }

    public BaseException(String module, String defaultMessage) {
        this(module, null, null, defaultMessage);
    }

    public BaseException(String code, Object[] args) {
        this(null, code, args, null);
    }

    public BaseException(String defaultMessage) {
        this(null, null, null, defaultMessage);
    }

    @Override
    public String getMessage() {
        String message = null;
        if (!StringUtils.isEmpty(code)) {
            message = MessageUtils.message(code, args);
        }
        return message;
    }
}
