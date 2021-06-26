package com.alpex.merchantdemo.until;


import lombok.Data;
import org.omg.CORBA.INTERNAL;

import java.util.HashMap;
import java.util.Map;

@Data

public class R {


    private Boolean success;

    private Integer code;

    private String message;

//    private Map<String,Object> data = new HashMap<>();

    private Object data;
    /**
     * 链式编程
     */

    private R(){}

    //操作成功，调用这个方法，返回成功的数据
    public static R ok(){
        R r = new R();
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS);
        r.setMessage("成功");
        return r;
    }

    public static R ok(Object value){

        return (R) value;
    }

    //操作失败，调用这个方法，返回失败的数据
    public static R error(){
        R r = new R();
        r.setSuccess(false);
        r.setCode(ResultCode.ERROR);
        r.setMessage("失败");
        return r;
    }

    public static R signError(Integer code,String message){
        R r = new R();
        r.setSuccess(false);
        r.setCode(code);
        r.setMessage(message);
        return r;
    }

    public static R error(Object value){
        return (R) value;
    }

    public  R success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public R message(String message){
        this.setMessage(message);
        return this;
    }

    public R code(Integer code){
        this.setCode(code);
        return this;
    }

//    public R data(String key, Object value){
//        this.data.put(key, value);
//        return this;
//    }

    public R data(Map<String, Object> map){
        this.setData(map);
        return this;
    }

    public R data( Object value){
        this.setData(value);
        return this;
    }

}
