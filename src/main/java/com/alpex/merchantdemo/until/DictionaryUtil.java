package com.alpex.merchantdemo.until;

import java.util.*;

public class DictionaryUtil {

    public static void main(String[] args) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("uid",1892422);
        params.put("uniqueCode","132456");
        params.put("money",152.01);
        params.put("payType",1);
        params.put("orderId", "31352236666543");
//        List<String> keys = new ArrayList<String>(params.keySet());
//        Collections.sort(keys);
//        String prestr = "";
//        for (int i = 0; i < keys.size(); i++) {
//            String key = keys.get(i);
//            Object value = params.get(key);
//            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
//                prestr = prestr + key + "=" + value;
//            } else {
//                prestr = prestr + key + "=" + value + "&";
//            }
//        }
        String ssss = SignUtil.newSign(params, "ssss");
//        prestr += "&key"+ssss;
//        System.out.println(prestr);
        System.out.println("=============="+ssss);

    }



    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params
     * 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

}
