package com.alpex.merchantdemo.vo;

import lombok.Data;

/**
 * 充值回调---订单状态
 */
@Data
public class OrderStatusVo {
    /**
     * 商户订单号
     */
    private String apiOrderNo;
    /**
     * 金额
     */
    private String money;
    /**
     * 交易状态(1成功，其他为失败)
     */
    private String tradeStatus;
    /**
     * 币富通订单
     */
    private String tradeId;
    /**
     * 唯一识别码
     */
    private String uniqueCode;

    /**
     * 验签串
     */
    private String signature;
}
