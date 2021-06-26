package com.alpex.merchantdemo.until;



import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtils {
    private Logger log = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * get
     *
     * @param host
     * @param path
     * @param headers
     * @param querys
     * @return
     * @throws Exception
     */
    public static String doGet(String host, String path,
                               Map<String, String> headers,
                               Map<String, String> querys) {
        HttpClient httpClient = wrapClient(host);
        HttpGet request = null;
        try {
            request = new HttpGet(buildUrl(host, path, querys));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (null != headers) {
            for (Map.Entry<String, String> e : headers.entrySet()) {
                request.addHeader(e.getKey(), e.getValue());
            }
        }

        String result = null;
        try {
            result = EntityUtils.toString(httpClient.execute(request).getEntity(), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * get
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @return
     * @throws Exception
     */
    public static HttpResponse doGet(String host, String path, String method,
                                     Map<String, String> headers,
                                     Map<String, String> querys)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpGet request = new HttpGet(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        return httpClient.execute(request);
    }

    /**
     * 获取返回结果
     *
     * @return
     */
    public static String getResponse(HttpResponse httpResponse) {
        String responseContent = null;
        try {

            InputStreamReader inputStreamReader = new InputStreamReader(httpResponse.getEntity().getContent(), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            responseContent = bufferedReader.readLine();
            while (responseContent != null) {
                stringBuffer.append(responseContent);
                responseContent = bufferedReader.readLine();
            }
            responseContent = stringBuffer.toString();
            return responseContent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseContent;
    }


    /**
     * post form
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param bodys
     * @return
     * @throws Exception
     */
    public static String doPost(String host, String path, String method,
                                Map<String, String> headers,
                                Map<String, String> querys,
                                Map<String, String> bodys)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (bodys != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

            for (String key : bodys.keySet()) {
                nameValuePairList.add(new BasicNameValuePair(key, bodys.get(key)));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            request.setEntity(formEntity);
        }
        return httpRequest(httpClient, request);

    }

    /**
     * Post String
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPost(String host, String path, String method,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      String body)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }


    /**
     * Post stream
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPost(String host, String path, String method,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      byte[] body)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (body != null) {
            request.setEntity(new ByteArrayEntity(body));
        }

        return httpClient.execute(request);
    }

    /**
     * Put String
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPut(String host, String path, String method,
                                     Map<String, String> headers,
                                     Map<String, String> querys,
                                     String body)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPut request = new HttpPut(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }

    /**
     * Put stream
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws Exception
     */
    public static HttpResponse doPut(String host, String path, String method,
                                     Map<String, String> headers,
                                     Map<String, String> querys,
                                     byte[] body)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpPut request = new HttpPut(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (body != null) {
            request.setEntity(new ByteArrayEntity(body));
        }

        return httpClient.execute(request);
    }

    /**
     * Delete
     *
     * @param host
     * @param path
     * @param method
     * @param headers
     * @param querys
     * @return
     * @throws Exception
     */
    public static HttpResponse doDelete(String host, String path, String method,
                                        Map<String, String> headers,
                                        Map<String, String> querys)
            throws Exception {
        HttpClient httpClient = wrapClient(host);

        HttpDelete request = new HttpDelete(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        return httpClient.execute(request);
    }

    private static String buildUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!StringUtils.isBlank(path)) {
            sbUrl.append(path);
        }
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }

        return sbUrl.toString();
    }

    private static HttpClient wrapClient(String host) {
        HttpClient httpClient = new DefaultHttpClient();
        if (host.startsWith("https://")) {
            sslClient(httpClient);
        }

        return httpClient;
    }

    private static void sslClient(HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] xcs, String str) {

                }

                public void checkServerTrusted(X509Certificate[] xcs, String str) {

                }
            };
            ctx.init(null, new TrustManager[]{tm}, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     * 获取返回结果
     *
     * @param httpClient
     * @param httpPost
     * @return
     */
    private static String httpRequest(HttpClient httpClient, HttpPost httpPost) {
        String responseContent = null;
        try {

            HttpResponse httpResponse = httpClient.execute(httpPost);
            InputStreamReader inputStreamReader = new InputStreamReader(httpResponse.getEntity().getContent(), "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            responseContent = bufferedReader.readLine();
            while (responseContent != null) {
                stringBuffer.append(responseContent);
                responseContent = bufferedReader.readLine();
            }
            responseContent = stringBuffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseContent;
    }

    /**
     * 发送POST请求
     * @param urlS
     * @param jsonStr
     * @return JSON或者字符串
     * @throws Exception
     */
    public static String sendPost(String urlS, String jsonStr) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlS); // url地址
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");// 设置请求方式为POST
            connection.setDoInput(true);// 允许读入
            connection.setDoOutput(true);// 允许写出
            connection.setUseCaches(false);// 不使用缓存
//            connection.setConnectTimeout(5000);// 连接超时时间
//            connection.setReadTimeout(10000);// 主机读取超时时间
            connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");// 设置参数类型是json格式
            connection.setRequestProperty("language","zh_cn");// 设置语言标识
//            connection.setRequestProperty("responseBodyNoEncryption","yes");// 设置语言标识
//            connection.setRequestProperty("signature",signature);// 设置签名
            connection.connect();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            writer.write(jsonStr);
            writer.close();

            int responseCode = connection.getResponseCode();
            InputStream inputStream = null;
            if(responseCode == HttpURLConnection.HTTP_OK){
                inputStream = connection.getInputStream();
            } else{

                JSONObject json = new JSONObject();
                json.put("code", responseCode);
                json.put("msg", connection.getResponseMessage());
				System.out.println("返回来的报文1："+json.toString());
//                {"msg":"Method Not Allowed","code":405}
                return json.toString();
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine())!= null) {
                response.append(line);
            }

			System.out.println("返回来的报文2："+response.toString());
//            {"code":-40004,"msg":"登录密码错误","result":null}
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (null!=reader){
                try{
                    reader.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            if (null!=connection){
                connection.disconnect();
            }
        }
    }
    /**
     * 发送POST请求
     * @param urlS
     * @param jsonStr
     * @return JSON或者字符串
     * @throws Exception
     */
    public static String sendSpecialPost(String urlS, String jsonStr) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlS); // url地址
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");// 设置请求方式为POST
            connection.setDoInput(true);// 允许读入
            connection.setDoOutput(true);// 允许写出
            connection.setUseCaches(false);// 不使用缓存
//            connection.setConnectTimeout(5000);// 连接超时时间
//            connection.setReadTimeout(10000);// 主机读取超时时间
            connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");// 设置参数类型是json格式
            connection.setRequestProperty("language","zh_cn");// 设置语言标识
            connection.setRequestProperty("responseBodyNoEncryption","yes");// 设置语言标识
//            connection.setRequestProperty("signature",signature);// 设置签名
            connection.connect();

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            writer.write(jsonStr);
            writer.close();

            int responseCode = connection.getResponseCode();
            InputStream inputStream = null;
            if(responseCode == HttpURLConnection.HTTP_OK){
                inputStream = connection.getInputStream();
            } else{

                JSONObject json = new JSONObject();
                json.put("code", responseCode);
                json.put("msg", connection.getResponseMessage());
                System.out.println("返回来的报文1："+json.toString());
//                {"msg":"Method Not Allowed","code":405}
                return json.toString();
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine())!= null) {
                response.append(line);
            }

            System.out.println("返回来的报文2："+response.toString());
//            {"code":-40004,"msg":"登录密码错误","result":null}
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (null!=reader){
                try{
                    reader.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            if (null!=connection){
                connection.disconnect();
            }
        }
    }

}