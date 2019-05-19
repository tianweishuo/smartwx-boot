package com.wxmp.core.util;

import java.io.IOException;
import java.io.Serializable;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author "ljx"
 *
 */
public class HttpClientSSLUtil implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private static Logger logger = LoggerFactory.getLogger(HttpClientSSLUtil.class);

    
    public static final String DEFAULT_CHARSET = "UTF-8";
    public static final Integer SUCCESS_CODE = 200;
    
    
    public static int get(String url, String cookie) {
        CloseableHttpClient httpClient = null;
        int statusCode = 0;
        try {
        	httpClient =createHttpClientL();
            HttpGet httpGet = new HttpGet(url);
            if (StringUtils.isNotBlank(cookie)) {
                httpGet.addHeader("Cookie", cookie);
            }
            HttpResponse response = httpClient.execute(httpGet); // 执行GET请求
            HttpEntity entity = response.getEntity(); // 获取响应实体
            statusCode = response.getStatusLine().getStatusCode();
            if (null != entity) {
                long contentLength = entity.getContentLength();
                EntityUtils.consume(entity);
                logger.info(contentLength + "");
            }
        } catch (Exception e) {
            logger.error("与[" + url + "]通信过程中发生异常,堆栈信息为", e);
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return statusCode;
    }

    public static String postJSON(String url,String params, Map<String, String> headers) {
        String responseContent = "通信失败";
        CloseableHttpClient httpClient = null;
        try {
        	httpClient =createHttpClientL();
            HttpPost httpPost = new HttpPost(url);
            if (headers != null && !headers.isEmpty()) {
            	for (Entry<String, String> entry : headers.entrySet()) {
            		httpPost.addHeader(entry.getKey(), entry.getValue());
            	}
            }
            httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
            httpPost.setEntity(new StringEntity(params, ContentType.APPLICATION_JSON));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, DEFAULT_CHARSET);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            logger.error("与[" + url + "]通信过程中发生异常,堆栈信息为", e);
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return responseContent;
    }


    public static String post(String url,Map<String, String> params, Map<String, String> headers) {
        String responseContent = "通信失败";
        CloseableHttpClient httpClient = null;
        try {
            httpClient =createHttpClientL();
            HttpPost httpPost = new HttpPost(url);
            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
            for (Entry<String, String> entry : params.entrySet()) {
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            if (headers != null && !headers.isEmpty()) {  
            	for (Entry<String, String> entry : headers.entrySet()) {  
            		httpPost.addHeader(entry.getKey(), entry.getValue());  
            	}  
            }
            httpPost.setEntity(new UrlEncodedFormEntity(formParams, DEFAULT_CHARSET));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, DEFAULT_CHARSET);
                EntityUtils.consume(entity);
            }
        } catch (Exception e) {
            logger.error("与[" + url + "]通信过程中发生异常,堆栈信息为", e);
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        logger.info(responseContent);
        return responseContent;
    }
    
    
    /**
     * HttpClientL  创建
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static  CloseableHttpClient  createHttpClientL() throws KeyManagementException, NoSuchAlgorithmException{
        SSLContext sslcontext = createIgnoreVerifySSL();
        /**
         * before http-core 4.4
         */
        SSLConnectionSocketFactory ssl = new SSLConnectionSocketFactory(sslcontext, new X509HostnameVerifier() {
            @Override
            public void verify(String host, SSLSocket ssl) throws IOException {
            }

            @Override
            public void verify(String host, X509Certificate cert) throws SSLException {
            }

            @Override
            public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {
            }

            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });

        /**
         * since http-core 4.4
         */
//        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE);
        return HttpClients.custom().setSSLSocketFactory(ssl).build();
    }
    
    
    
    /**
     * 绕过验证
     *
     * @return
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    public static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sc = SSLContext.getInstance("SSLv3");
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    X509Certificate[] paramArrayOfX509Certificate,
                    String paramString) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sc.init(null, new TrustManager[]{trustManager}, new java.security.SecureRandom());
        return sc;
    }

    public static void main(String[] args) {
        String url = "https://10.70.69.138:8443/credit_multi_risk";

        JSONObject paramsJson= new JSONObject();
//        {"sign":"66BB628A366CB7A372234F90A1644461","org_id":"E7AA85B1AFD840F896BC6EC9AFF00A9D",
//                "data":{"name":"张三","mobile":"13800138000","cid":"123456199001011233","queryType":"1"}}

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name","张三");
        jsonObject.put("mobile","13800138000");
        jsonObject.put("cid","123456199001011233");
        jsonObject.put("queryType","1");
        paramsJson.put("sign","66BB628A366CB7A372234F90A1644461");
        paramsJson.put("org_id","E7AA85B1AFD840F896BC6EC9AFF00A9D");
        paramsJson.put("data",jsonObject);
        String string = postJSON(url,paramsJson.toJSONString(),null);
        System.out.println(string);
    }
}
