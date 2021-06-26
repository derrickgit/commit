package com.alpex.merchantdemo.until;

//定义返回数据使用的状态码
public interface ResultCode {

    int SUCCESS = 1;//成功状态码
    int ERROR = -1;//失败状态码
    int AUTH = 30000;//没有操作权限的状态码
    int SIGN_VERIFICATION_FAILED =16000;//签证失败
    String SIGN_VERIFICATION_FAILED_Message="验签失败";

}
