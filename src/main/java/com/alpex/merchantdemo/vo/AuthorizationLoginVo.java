package com.alpex.merchantdemo.vo;

import lombok.Data;

/**
 * 授权登录
 */
@Data
public class AuthorizationLoginVo {

    /**
     * 商户uid
     */
    private String uid;
    /**
     * 唯一识别码
     */
    private String uniqueCode;
    /**
     * 签名
     */
    private String signature;
    /**
     * 接入秘钥
     */
    private String accessSecret;
}
