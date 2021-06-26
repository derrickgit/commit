package com.alpex.merchantdemo.service;

import com.alpex.merchantdemo.until.R;
import com.alpex.merchantdemo.vo.AuthorizationLoginVo;
import com.alpex.merchantdemo.vo.CounterVo;
import com.alpex.merchantdemo.vo.OrderStatusVo;
import com.alpex.merchantdemo.vo.PaymentVo;

public interface MerchantDemoService {
    R counter(CounterVo counterVo);

    R authorizationLogin(AuthorizationLoginVo authorizationLoginVo);


    R intoTheVault(PaymentVo paymentVo);

    R orderStatus(OrderStatusVo orderStatusVo);

}
