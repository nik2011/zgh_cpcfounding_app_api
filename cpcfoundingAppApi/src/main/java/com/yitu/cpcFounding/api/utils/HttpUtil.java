package com.yitu.cpcFounding.api.utils;

import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * http工具类
 *
 * @author yaoyanhua
 * @version 1.0
 * @date 2020/6/11
 */
@Component
public class HttpUtil {

    @Value("${file.image.host}")
    private String fileHost;

    @Value("${file.image.webHost}")
    private String webHost;

    private static String fileHost_ = null;

    private static OkHttpClient _okHttpClient = null;

    private static String _webHost = null;

    @Autowired
    private OkHttpClient okHttpClient;

    @PostConstruct
    public void getFileHost() {
        fileHost_ = this.fileHost;
        _okHttpClient = this.okHttpClient;
        _webHost = this.webHost;
    }


    private static Logger log = LoggerFactory.getLogger(HttpUtil.class);

    /**
     * 发送请求
     *
     * @param url     请求地址
     * @param body    参数
     * @param headers 请求头
     * @author yaoyanhua
     * @date 2020/6/23 17:53
     */
    public static Response postRequest(String url, RequestBody body, Headers headers) {
        Response response = null;
        try {
            Request.Builder builder = new Request.Builder()
                    .url(url)
                    .post(body);
            if (headers != null) {
                builder.headers(headers);
            }
            Request request = builder.build();
            Call call = _okHttpClient.newCall(request);
            response = call.execute();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return response;
    }

    /**
     * 发送请求
     *
     * @param url  请求地址
     * @param body 参数
     * @return okhttp3.Response
     * @author chenpinjia
     * @date 2021/1/23 17:25
     */
    public static Response postRequest(String url, RequestBody body) {
        return postRequest(url, body, null);
    }

    /**
     * @param urlPath
     * @description: get请求
     * @return: String
     * @author: luzhichao
     * @date: 2021/1/25 17:06
     */
    public static String getRequest(String urlPath, Map<String, Object> param) {
        urlPath = urlPath + "?" + mapToUrlParam(param);
        HttpGet get = new HttpGet(urlPath);
        String result = null;
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result = getHttpEntityContent(response);
            }
        } catch (Exception e) {
            log.error("发送请求异常！" + e.getMessage());
        } finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
            }
        }
        log.info("返回参数>>>>>>>>:  " + result);
        return result;
    }

    public static String getHttpEntityContent(HttpResponse response) {
        String result = "";
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream in = null;
            BufferedReader br = null;
            try {
                in = entity.getContent();
                br = new BufferedReader(new InputStreamReader(in, "utf-8"));
                StringBuilder strBer = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    strBer.append(line + '\n');
                }
                result = strBer.toString();
                result = result.replaceAll("\n", "");
            } catch (IOException e) {
                log.error(e.getMessage());
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException e) {
                    log.error(e.getMessage());
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                }
            }
        }
        return result;
    }

    private static String mapToUrlParam(Map<String, Object> map) {
        if (map == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = StringUtils.substringBeforeLast(s, "&");
        }
        return s;
    }

    /**
     * 获取post请求的RequestBody对象
     *
     * @param map
     * @return okhttp3.RequestBody
     * @author J.K
     * @date 2019/11/19 14:38
     */
    public static RequestBody getBody(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        String params = "";
        map = sortMapByKey(map);
        if (map == null) {
            return null;
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            builder.add(key, value);
            params += key + "=" + value + "&";
        }
        return builder.build();
    }

    /**
     * 排序map
     *
     * @param map
     * @return java.util.Map<java.lang.String, java.lang.String>
     * @author yaoyanhua
     * @date 2020/6/23 17:57
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, String> sortMap = new TreeMap<>(
                new MapKeyComparator());
        sortMap.putAll(map);
        return sortMap;
    }

    /**
     * MapKeyComparator
     *
     * @author yaoyanhua
     * @return
     * @date 2020/6/23 17:57
     */
    static class MapKeyComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }

    /**
     * 获取客户端ip
     *
     * @param request request
     * @return java.lang.String
     * @author yaoyanhua
     * @date 2020/6/23 17:58
     */
    public static String getIPAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        if (ip != null && ip.indexOf(",") != -1) {
            String[] ipWithMultiProxy = ip.split(",");

            for (int i = 0; i < ipWithMultiProxy.length; ++i) {
                String eachIpSegement = ipWithMultiProxy[i];
                if (!"unknown".equalsIgnoreCase(eachIpSegement)) {
                    ip = eachIpSegement;
                    break;
                }
            }
        }
        return ip;
    }

    /**
     * 是否ajax请求
     *
     * @param request request
     * @return boolean
     * @author yaoyanhua
     * @date 2020/6/23 17:58
     */
    public static boolean isAjaxRequest(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        String requestedWith = request.getHeader("x-requested-with");
        if (requestedWith != null && requestedWith.equalsIgnoreCase("XMLHttpRequest")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取项目网络路径
     *
     * @param request request
     * @return java.lang.String
     * @author yaoyanhua
     * @date 2020/6/23 17:58
     */
    public static String getNetPath(HttpServletRequest request) {
        final String contextPath = request.getContextPath();
        int serverPort = request.getServerPort();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + contextPath + "/";
    }

    /**
     * 获取项目网络路径不带上下文
     *
     * @param request
     * @return java.lang.String
     * @author yaoyanhua
     * @date 2020/6/23 17:58
     */
    public static String getNetPathWithoutContextPath(HttpServletRequest request) {
        int serverPort = request.getServerPort();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/";
    }


    /**
     * 获取图片文件网络路径
     *
     * @param request
     * @param filePath
     * @return java.lang.String
     * @author yaoyanhua
     * @date 2020/6/23 17:59
     */
    public static String getNetResourcePath(HttpServletRequest request, String filePath) {
        if (StringUtils.isBlank(filePath) || filePath.startsWith("http")) {
            return filePath;
        }
        if (filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }
        return fileHost_ + filePath;
    }

    /**
     * 获取本地图片文件网络路径
     *
     * @param filePath
     * @return java.lang.String
     * @author yaoyanhua
     * @date 2020/6/23 17:59
     */
    public static String getWebResourcePath(String filePath) {
        if (StringUtils.isBlank(filePath) || filePath.startsWith("http")) {
            return filePath;
        }
        if (filePath.startsWith("/")) {
            filePath = filePath.substring(1);
        }
        return _webHost + filePath;
    }


}
