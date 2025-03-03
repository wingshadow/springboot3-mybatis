package com.hawk.framework.enums;

import com.hawk.framework.encrypt.encryptor.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 算法名称
 *
 * @author 老马
 * @version 4.6.0
 */
@Getter
@AllArgsConstructor
public enum AlgorithmType {

    /**
     * 默认走yml配置
     */
    DEFAULT(null),

    /**
     * base64
     */
    BASE64(Base64Encryptor.class),

    /**
     * aes
     */
    AES(AesEncryptor.class),

    /**
     * rsa
     */
    RSA(RsaEncryptor.class),

    /**
     * sm4
     */
    SM4(Sm4Encryptor.class);

    private final Class<? extends AbstractEncryptor> clazz;
}
