package com.alpex.merchantdemo.until;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Charsets;
import com.google.common.collect.Ordering;
import com.google.common.hash.Hashing;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * 签名类
 */
public class SignUtil {
    private static final Integer RANDOM = 652;
    public static final String AccessKey = "vSyY7k75470XTzI58mBkzgcGzVLLfqJV";

    /**
     * 验证签名
     *
     * @param request 请求
     * @param signKey 私钥
     * @return
     */
    public static boolean verify(HttpServletRequest request, String signKey) {
        //获取所有请求参数
        if (null == request || null == signKey) {
            return false;
        }
        Enumeration<String> paramKeys = request.getParameterNames();
        Map<String, Object> params = new HashMap<>();
        while (paramKeys.hasMoreElements()) {
            String key = paramKeys.nextElement();
            params.put(key, request.getParameter(key));
        }
        if (!params.containsKey("signature")) {
            return false;
        } else {
            //获取当前key
            String key = params.remove("signature").toString();
            //对参数进行加密
            String currentKey = sign(params, signKey);
            //对比
            return Objects.equals(key, currentKey);
        }
    }

    /**
     * 验证签名
     *
     * @param params 请求
     * @param signKey 私钥
     * @return
     */
    public static boolean verify(Map<String, Object> params, String signKey) {

        if (!params.containsKey("signature")) {
            return false;
        } else {
            //获取当前key
            String key = params.remove("signature").toString();
            //对参数进行加密
            String currentKey = sign(params, signKey);
            //对比
            return Objects.equals(key, currentKey);
        }
    }
    //生成签名
    public static String sign(Map<String, Object> params, String key) {
        List<String> keys = Ordering.usingToString().sortedCopy(params.keySet());//key排序
        StringBuilder sb = new StringBuilder();
        for (String k : keys) {
            //时间再减个随机数
            if (k.equals("time")) {
                Long timestamp = (Long) params.get(k);
                sb.append(k).append("=").append((timestamp - RANDOM)).append("&");
            } else {
                sb.append(k).append("=").append(params.get(k)).append("&");
            }

        }
        sb.append("key").append("=").append(key);
        System.out.println("验签str:" + sb.toString());
        return DigestUtils.md5DigestAsHex(sb.toString().getBytes());
    }
    /**
     * 验证签名
     *
     * @param params 请求
     * @param signKey 私钥
     * @return
     */
    public static boolean newVerify(Map<String, Object> params, String signKey) {

        if (!params.containsKey("signature")) {
            return false;
        } else {
            //获取当前key
            String key = params.remove("signature").toString();
            //对参数进行加密
            String currentKey = newSign(params, signKey);
            //对比
            return Objects.equals(key, currentKey);
        }
    }
    //生成签名
    public static String newSign(Map<String, Object> params, String key) {
        List<String> keys = Ordering.usingToString().sortedCopy(params.keySet());//key排序
        StringBuilder sb = new StringBuilder();
        for (String k : keys) {
            //时间再减个随机数
            if (k.equals("time")) {
                Long timestamp = (Long) params.get(k);
                sb.append(k).append("=").append((timestamp - RANDOM)).append("&");
            } else {
                sb.append(k).append("=").append(JSON.toJSONString(params.get(k), SerializerFeature.SortField.MapSortField)).append("&");
            }

        }
        sb.append("key").append("=").append(key);
        System.out.println("newSign验签参数str:" + sb.toString());
        String md5=DigestUtils.md5DigestAsHex(sb.toString().getBytes());
        System.out.println("newSign签名:" + md5);
        return md5;
    }
    public static String md5pwdSalt(String md5pwd, String salt) {
        StringBuilder sb = new StringBuilder();
        sb.append(md5pwd.toLowerCase()).append(salt);
        return Hashing.md5().hashString(sb, Charsets.UTF_8).toString();
    }

    /**
     * 根据文件计算出文件的MD5
     *
     * @param in
     * @return
     */
    public static String getFileMD5(InputStream in) {

        MessageDigest digest = null;


        byte buffer[] = new byte[1024 * 10];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");

            while ((len = in.read(buffer, 0, 1024* 10)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());

        return bigInt.toString(16);
    }

    public static String getStringMD5(String str) {

        MessageDigest digest = null;

        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");

            buffer = str.getBytes();
            digest.update(buffer, 0, buffer.length);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());

        return bigInt.toString(16);
    }

    public static String getTimeStamp() {
        long timeStamp = System.currentTimeMillis() / 1000;
        return String.valueOf(timeStamp);
    }

    public static void main(String[] args) {

//        String s = md5pwdSalt("96e79218965eb72c92a549dd5a330112", "Z0JqJ8");
//        System.out.println(Hashing.md5().hashString("Alpha1010", Charsets.UTF_8).toString());
//        Map<String,String> map = new HashMap<>();
//        map.put("aserId","120214");
//        map.put("uid","1234567");
//        map.put("phone","13524215542");
//        map.put("bphoneAreaCode","+86");
//        map.put("timeStamp", String.valueOf(new Date().getTime()));
//        String signature = sign(map,AccessKey);
//        map.put("signature",signature);
//        if(verify(map,AccessKey)){
//            System.out.println("验签通过");
//        }

    }
}
