package com.yanming.test.java;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

/**
* @author yanming
* @description client 请求
* @date 2018/10/15 12:02
*/
public class JavaClient {

    public static String urlName = "https://xx/xx";
    public static void main(String[] args) throws Throwable {

        String postcontent = "发送内容testtstestststst";
        byte[] b = post(urlName,"utf-8",null,20000,postcontent);
        System.out.println("返回"+new String(b));
    }

    public static byte[] post(String url, String charset,
                              Map<String, String> headers, int timeout, String postData) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);
        if (null != headers) {
            for (String key : headers.keySet()) {
                httppost.setHeader(key, headers.get(key));
            }
        }
        // 配置请求的超时设置
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(timeout)
                .setConnectTimeout(timeout).setSocketTimeout(timeout).build();
        httppost.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        byte[] data = new byte[0];
        try {
            httppost.setEntity(new StringEntity(postData, charset));
            response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();
            data = EntityUtils.toByteArray(resEntity);
            EntityUtils.consume(resEntity);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            httpclient.close();
            httpclient.getConnectionManager().shutdown();
        }
        return data;
    }
}
