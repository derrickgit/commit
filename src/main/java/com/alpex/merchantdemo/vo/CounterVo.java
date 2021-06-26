package com.alpex.merchantdemo.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 跳转收银台
 */
@Data
public class CounterVo {
    /**
     * 商户uid
     */
    private String uid;
    /**
     * 用户唯一标识
     */
    private String uniqueCode;
    /**
     * 金额
     */
    private BigDecimal money;
    /**
     * 支付类型(暂时只限定1----银行卡)
     */
    private Integer payType;
    /**
     * 订单号(商户订单)
     */
    private String orderId;
    /**
     * 签名
     */
    private String signature;
    /**
     * 接入秘钥
     */
    private String accessSecret;



}
