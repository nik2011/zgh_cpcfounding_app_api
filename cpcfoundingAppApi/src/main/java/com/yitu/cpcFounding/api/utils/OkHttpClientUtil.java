package com.yitu.cpcFounding.api.utils;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * @descraption
 * @outhor liuzhaowei
 * @create 2019-04-23 15:15
 */

@Component
public class OkHttpClientUtil {
    public static final MediaType JSON = MediaType.parse("application/json");
    public static final MediaType FROM = MediaType.parse("application/x-www-form-urlencoded");

    public String httpGet(String url) {
        OkHttpClient httpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = httpClient.newCall(request).execute();
            return response.body().string(); // 返回的是string 类型，json的mapper可以直接处理
        } catch (IOException e) {
            e.printStackTrace();
            Logger log = LoggerFactory.getLogger(this.getClass());
            log.error(String.format("get请求失败：%s;%s", e.getMessage(), e.getStackTrace()));
        }
        return null;
    }

    /**
     * @param url       地址
     * @param params    请求参数
     * @param paramType 参数类型  1  json  2  x-www-form-urlencoded
     * @return java.lang.String
     * @Description:
     * @author liuzhaowei
     * @date 2021/1/28
     */
    public String httpPost(String url, final Map<String, Object> params, int paramType) {
        OkHttpClient httpClient = new OkHttpClient();
        RequestBody requestBody = null;
        if (paramType == 1) {
            String paramsStr = JSONObject.toJSONString(params);
            requestBody = RequestBody.create(JSON, paramsStr);
        } else {
            FormBody.Builder formBodyBuilder = new FormBody.Builder();
            if (params != null) {
                Set<String> keySet = params.keySet();
                for (String key : keySet) {
                    String value = String.valueOf(params.get(key));
                    formBodyBuilder.add(key, value);
                }
            }
            requestBody = formBodyBuilder.build();
        }
        try {
            Request request = new Request.Builder()
                    .url(url)
                    //.addHeader("accept","application/json")
                    //.addHeader("contentType","application/json")
                    .post(requestBody)
                    .build();
            Response response = httpClient.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            Logger log = LoggerFactory.getLogger(this.getClass());
            log.error(String.format("post请求失败：%s;%s,%s", url, e.getMessage(), e.getStackTrace()));
        }
        return null;
    }
}