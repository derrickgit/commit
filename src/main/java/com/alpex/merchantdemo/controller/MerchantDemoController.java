package com.alpex.merchantdemo.controller;

import com.alibaba.fastjson.JSONObject;
import com.alpex.merchantdemo.service.MerchantDemoService;
import com.alpex.merchantdemo.until.HttpUtils;
import com.alpex.merchantdemo.until.R;
import com.alpex.merchantdemo.vo.AuthorizationLoginVo;
import com.alpex.merchantdemo.vo.CounterVo;
import com.alpex.merchantdemo.vo.OrderStatusVo;
import com.alpex.merchantdemo.vo.PaymentVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/coin/pay")
@CrossOrigin
public class MerchantDemoController {


    @Resource
    private MerchantDemoService merchantDemoService;

    /**
     * 授权登录
     *
     * @param authorizationLoginVo
     * @return
     */
    @PostMapping("/user/pay/authorization-login")
    public R authorizationLogin(@RequestBody AuthorizationLoginVo authorizationLoginVo) {
        return merchantDemoService.authorizationLogin(authorizationLoginVo);
    }

    /**
     * 跳转收银台
     *
     * @param counterVo
     * @return
     */
    @PostMapping("/order/pay/checkout/counter")
    @ResponseBody
    public R counter(@RequestBody CounterVo counterVo) {

        return merchantDemoService.counter(counterVo);
    }

    /**
     * 订单状态-----充值回调  法币回调
     *
     * @return
     */
    @PostMapping("/orderStatus")
    public R rechargeCallBack(@Validated @RequestBody OrderStatusVo orderStatusVo) {
        return merchantDemoService.orderStatus(orderStatusVo);
    }

    /**
     * 入金------币币回调
     *
     * @return
     */
    @PostMapping("/intoTheVault")
    public R coinCallBack(@RequestBody PaymentVo paymentVo) {
        {
            return merchantDemoService.intoTheVault(paymentVo);
        }

    }

}
