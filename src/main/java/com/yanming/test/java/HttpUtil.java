package com.yanming.test.java;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtil {

    public static final String RESPONSE_BODY = "responseBody";

    public static final String RESPONSE_HEADER = "responseHeader";

    public Map post(String url, List<NameValuePair> nvps, String charset,
                    Map<String, String> headers, int timeout, String postData) throws Exception {
        CloseableHttpClient httpclient = getClient(url);
        Map map = new HashMap();
        HttpPost httpPost = null;
        CloseableHttpResponse response = null;
        try {
            byte[] data = new byte[0];
            httpPost = new HttpPost(url);
            if (null != headers) {
                for (String key : headers.keySet()) {
                    httpPost.setHeader(key, headers.get(key));
                }
            }
            // 配置请求的超时设置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(timeout)
                    .setConnectTimeout(timeout).setSocketTimeout(timeout).build();
            httpPost.setConfig(requestConfig);

            if (null == postData) {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps, charset));
            } else {
                httpPost.setEntity(new StringEntity(postData, charset));
            }

            response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            data = EntityUtils.toByteArray(entity);
            map.put(RESPONSE_BODY,data);
            map.put(RESPONSE_HEADER,response.getAllHeaders());
            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                response.close();
            }
            if (httpPost != null) {
                httpPost.releaseConnection();
            }
            if (httpclient != null) {
                httpclient.close();
            }
        }
        return map;
    }


    private CloseableHttpClient getClient(String url) throws Exception {
        if(url.startsWith("https")){
            //忽略证书校验
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{truseAllManager}, null);
            // SSL套接字连接工厂,NoopHostnameVerifier为信任所有服务器
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sc,NoopHostnameVerifier.INSTANCE);
            /**
             * 通过setSSLSocketFactory(sslsf)保证httpclient实例能发送Https请求
             */
            return HttpClients.custom().setSSLSocketFactory(sslsf).build();
        }else {
            return HttpClients.createDefault();
        }
    }

    public static TrustManager truseAllManager = new X509TrustManager() {

        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }
    };
}

