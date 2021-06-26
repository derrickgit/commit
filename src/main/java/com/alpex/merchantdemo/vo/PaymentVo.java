package com.alpex.merchantdemo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 币币回调----入金
 */
@Data
@NoArgsConstructor
public class PaymentVo {

    /**
     * 验签串
     */
    private String signature;
    /**
     * 唯一识别码
     */
    private String uniqueCode;
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 数量
     */
    private String amount;
    /**
     * 币种名称
     */
    private String coinName;
}
