package com.alpex.merchantdemo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alpex.merchantdemo.service.MerchantDemoService;
import com.alpex.merchantdemo.until.HttpUtils;
import com.alpex.merchantdemo.until.R;
import com.alpex.merchantdemo.until.ResultCode;
import com.alpex.merchantdemo.until.SignUtil;
import com.alpex.merchantdemo.vo.AuthorizationLoginVo;
import com.alpex.merchantdemo.vo.CounterVo;
import com.alpex.merchantdemo.vo.OrderStatusVo;
import com.alpex.merchantdemo.vo.PaymentVo;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@Service
public class MerchantDemoImpl implements MerchantDemoService {

//    private static final String accessSecret ="GFCMOaSK70wAQ9q8fV5FSCaThefa49HQ";
    //线上环境
//    private static final String backSecret ="cELZwuBmwcza10aKrj2ozAYtuxI0G8jo";
    //测试环境
    private static final String backSecret ="RbYgPgHEwKbK4VEyBl6vN0EUAXcd1ndF";

    @Override
    public R counter(CounterVo counterVo) {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()) +":跳转收银台入参："+counterVo);
        Map map = JSONObject.parseObject(JSON.toJSONString(counterVo), Map.class);//实体转化为map
        //传入的参数不能为空
        if (map.size()>0&&map!=null) {
            if(map.get("signature")!=null){
                map.remove("signature");
            }
            if(map.get("accessSecret")!=null){
                map.remove("accessSecret");
            }

            //生签  map为uid uniqueCode money payType orderId，         counterVo.getAccessSecret()为接入秘钥
            String signkey = SignUtil.sign(map, counterVo.getAccessSecret());
            System.out.println(signkey);
            counterVo.setSignature(signkey);

                //接受前端给的参数
        JSONObject params =(JSONObject) JSONObject.toJSON(counterVo);  //实体对象转换JsonObject对象
            params.remove("accessSecret");
                //调用远程接口
                //线上环境
//                String url="https://api.bifutong.vip/coin/pay/order/pay/checkout/counter";
                //测试环境
                  String url="http://8.210.205.80:17425/coin/pay/order/pay/checkout/counter";
                String response = HttpUtils.sendPost(url, params.toString());
                JSONObject parse = (JSONObject) JSONObject.parse(response);
                Integer code = (Integer) parse.get("code");
                Boolean success = (Boolean) parse.get("success");

                if (code == 1 && success) {
                    //如果远程接口执行成功
                    R r = JSONObject.parseObject(response, R.class);
                    return r;
                } else {
                    R r = JSONObject.parseObject(response, R.class);
                    return r;
                }

        } else {
            return R.error();
        }


    }

    @Override
    public R authorizationLogin(AuthorizationLoginVo authorizationLoginVo) {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())+":授权登录入参："+authorizationLoginVo);
        Map map = JSONObject.parseObject(JSON.toJSONString(authorizationLoginVo), Map.class);//实体转化为map
        //传入的参数不能为空
        if(map.size()>0&&map!=null){

            if(map.get("signature")!=null){
                map.remove("signature");
            }
            if(map.get("accessSecret")!=null){
                map.remove("accessSecret");
            }

            //生签    authorizationLoginVo.getAccessSecret()为接入秘钥
            String signkey = SignUtil.sign(map, authorizationLoginVo.getAccessSecret());
            System.out.println(signkey);
            authorizationLoginVo.setSignature(signkey);

            //接受前端给的参数
            JSONObject params =(JSONObject) JSONObject.toJSON(authorizationLoginVo);  //实体对象转换JsonObject对象
            params.remove("accessSecret");

            //调用远程接口
//            http://8.210.205.80:17425/coin/pay/user/pay/authorization-login
            //线上环境
//            String url="https://api.bifutong.vip/coin/pay/user/pay/authorization-login";
//            String url="http://localhost:17425/coin/pay/user/pay/authorization-login";
            //测试环境
           String url="http://8.210.205.80:17425/coin/pay/user/pay/authorization-login";
            String response = HttpUtils.sendPost(url, params.toString());
            JSONObject parse = (JSONObject) JSONObject.parse(response);
            Integer code = (Integer) parse.get("code");
            Boolean success = (Boolean) parse.get("success");
            if (code == 1 && success) {
                //如果远程接口执行成功
                R r = JSONObject.parseObject(response, R.class);
                return r;
            } else {
                R r = JSONObject.parseObject(response, R.class);
                return r;
            }
        }else{
            System.out.println("入参为空");
            return R.error();
        }



    }

    /**
     * 入金
     * @param paymentVo
     * @return
     */
    @Override
    public R intoTheVault(PaymentVo paymentVo) {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())+":入金入参为:"+paymentVo);
        Map map = JSONObject.parseObject(JSON.toJSONString(paymentVo), Map.class);//实体转化为map

        //传入的参数不能为空
        if(map.size()>0&&map!=null) {
            //判断验签串是否有值
            if (paymentVo.getSignature() != null&&!paymentVo.getSignature().equals("")) {

                //验签    backSecret回调秘钥
                boolean verify = SignUtil.verify(map, backSecret);
                if (verify) {
                    //验签成功
                    System.out.println("参数:"+paymentVo);
                    return R.ok();
                } else {
                    System.out.println("参数:"+paymentVo);
                    return R.signError(ResultCode.SIGN_VERIFICATION_FAILED,ResultCode.SIGN_VERIFICATION_FAILED_Message);
                }

            } else {
                System.out.println("参数:"+paymentVo);
                return R.signError(ResultCode.SIGN_VERIFICATION_FAILED,ResultCode.SIGN_VERIFICATION_FAILED_Message);
            }
        }else{
            System.out.println("入参为空:"+paymentVo);
            return R.error();
        }

    }

    @Override
    public R orderStatus(OrderStatusVo orderStatusVo) {
        System.out.println(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date())+":订单状态入参为:"+orderStatusVo);
        //实体转化为map
        Map map = JSONObject.parseObject(JSON.toJSONString(orderStatusVo), Map.class);
        //传入的参数不能为空
        if(map.size()>0&&map!=null){
            System.out.println("参数:"+orderStatusVo);
            //验签   backSecret回调秘钥
            if (orderStatusVo.getSignature() != null&&!orderStatusVo.getSignature().equals("")) {
                boolean verify = SignUtil.verify(map, backSecret);
                if (verify) {
                    //验签成功
                    System.out.println("参数:" + orderStatusVo);
                    return R.ok();
                } else {
                    System.out.println("参数:" + orderStatusVo);
                    return R.signError(ResultCode.SIGN_VERIFICATION_FAILED, ResultCode.SIGN_VERIFICATION_FAILED_Message);
                }
            }else{
                System.out.println("参数:"+orderStatusVo);
                return R.signError(ResultCode.SIGN_VERIFICATION_FAILED,ResultCode.SIGN_VERIFICATION_FAILED_Message);
            }

        }else{
            System.out.println("入参为空:"+orderStatusVo);
            return R.error();
        }


    }


}
