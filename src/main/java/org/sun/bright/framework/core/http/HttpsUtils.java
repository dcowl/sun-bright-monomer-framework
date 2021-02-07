package org.sun.bright.framework.core.http;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * https请求（忽略证书）工具类
 *
 * @author <a href="mailto:2867665887@qq.com">dcowl</a>
 * @version 1.0
 */
@Slf4j
public class HttpsUtils {

    private HttpsUtils() {
    }

    private static final String HTTP = "http";
    private static final String HTTPS = "https";

    private static SSLConnectionSocketFactory ssl = null;
    private static PoolingHttpClientConnectionManager cm = null;

    // HTTPS需要代码块内容
    static {
        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            // 全部信任 不做身份鉴定
            builder.loadTrustMaterial(null, (TrustStrategy) (x509Certificates, s) -> true);
            ssl = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register(HTTP, new PlainConnectionSocketFactory())
                    .register(HTTPS, ssl)
                    .build();
            cm = new PoolingHttpClientConnectionManager(registry);
            // max connection
            cm.setMaxTotal(200);
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            log.error(" init Https Client Error, info {} ", e.getMessage(), e);
        }
    }

    /**
     * httpClient post请求
     *
     * @param url    请求url
     * @param header 头部信息
     * @param param  请求参数 form提交适用
     * @param entity 请求实体 json/xml提交适用
     * @return 可能为空 需要处理
     */
    public static String post(String url, Map<String, String> header, Map<String, Object> param, HttpEntity entity) {
        try (CloseableHttpClient httpClient = getHttpClient()) {
            HttpPost httpPost = new HttpPost(url);
            // 设置头信息
            setHeader(httpPost, header);
            // 设置请求参数
            if (param != null) {
                List<NameValuePair> formParams = new ArrayList<>();
                for (Map.Entry<String, Object> entry : param.entrySet()) {
                    //给参数赋值
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
                httpPost.setEntity(urlEncodedFormEntity);
            }
            // 设置实体 优先级高
            if (entity != null) {
                httpPost.setEntity(entity);
            }
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            String result;
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = httpResponse.getEntity();
                result = EntityUtils.toString(resEntity);
            } else {
                result = readHttpResponse(httpResponse);
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * httpClient get请求
     *
     * @param url         请求url
     * @param paramHeader 头部信息
     * @return 可能为空 需要处理
     */
    public static String get(String url, Map<String, String> paramHeader) {
        try (CloseableHttpClient httpClient = getHttpClient()) {
            HttpGet httpGet = new HttpGet(url);
            setHeader(httpGet, paramHeader);
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            String result;
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity resEntity = response.getEntity();
                result = EntityUtils.toString(resEntity, Consts.UTF_8);
            } else {
                result = readHttpResponse(response);
            }
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    private static void setHeader(HttpRequestBase request, Map<String, String> header) {
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
    }

    private static CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .setSSLSocketFactory(ssl)
                .setConnectionManager(cm)
                .setConnectionManagerShared(true)
                .build();
    }

    private static String readHttpResponse(HttpResponse httpResponse) throws IOException {
        StringBuilder builder = new StringBuilder();
        // 获取响应消息实体
        HttpEntity entity = httpResponse.getEntity();
        // 响应状态
        builder.append("status:").append(httpResponse.getStatusLine());
        builder.append("headers:");
        HeaderIterator iterator = httpResponse.headerIterator();
        while (iterator.hasNext()) {
            builder.append("\t").append(iterator.next());
        }
        // 判断响应实体是否为空
        if (entity != null) {
            String response = EntityUtils.toString(entity);
            builder.append("response length :").append(response.length());
            builder.append("response content: ").append(response.replace("\r\n", ""));
        }
        return builder.toString();
    }

}
