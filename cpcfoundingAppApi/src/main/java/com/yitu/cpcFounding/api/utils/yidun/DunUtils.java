package com.yitu.cpcFounding.api.utils.yidun;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.yitu.cpcFounding.api.exception.GlobalException;
import org.apache.http.Consts;
import org.apache.http.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 易盾工具类
 *
 * @author shenjun
 * @date 2021/1/22 17:04
 */
public class DunUtils {

    private static Logger log = LoggerFactory.getLogger(GlobalException.class);

    /**
     * 产品密钥ID，产品标识
     */
    private final static String SECRETID = "0d878d59990de53d7b097a3e14590fd9";
    /**
     * 产品私有密钥，服务端生成签名信息使用，请严格保管，避免泄露
     */
    private final static String SECRETKEY = "9222ed7148a864880a60442379ec9726";
    /**
     * 业务ID，易盾根据产品业务特点分配
     */
    private final static String BUSINESSID_TEXT = "f14ac2c760cfd15bebba4b6c12622bd6";
    private final static String BUSINESSID_IMG = "6561b0412001fd8793c5b9d8cecc4d13";
    /**
     * 易盾反垃圾云服务文本在线检测接口地址
     */
    private final static String API_URL_TEXT = "http://as.dun.163.com/v4/text/check";
    private final static String API_URL_IMG = "http://as.dun.163.com/v4/image/check";

    /**
     * 实例化HttpClient，发送http请求使用，可根据需要自行调参
     */
    private static HttpClient httpClient = HttpClient4Utils.createHttpClient(100, 20, 2000, 2000, 2000);


    /**
     * 检查文本
     *
     * @param text 文本
     * @return void
     * @author shenjun
     * @date 2021/1/22 17:10
     */
    public static DunResult checkText(String text) {
        Map<String, String> params = new HashMap<String, String>();
        // 1.设置公共参数
        params.put("secretId", SECRETID);
        params.put("businessId", BUSINESSID_TEXT);
        params.put("version", "v4");
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("nonce", String.valueOf(new Random().nextInt()));
        // MD5, SM3, SHA1, SHA256
        params.put("signatureMethod", "MD5");
        // 2.设置私有参数
        params.put("dataId", "ebfcad1c-dba1-490c-b4de-e784c2691768");
        params.put("content", text);
        // 3.生成签名信息
        String signature = SignatureUtils.genSignature(SECRETKEY, params);
        params.put("signature", signature);
        // 4.发送HTTP请求，这里使用的是HttpClient工具包，产品可自行选择自己熟悉的工具包发送请求
        String response = HttpClient4Utils.sendPost(httpClient, API_URL_TEXT, params, Consts.UTF_8);
        // 5.解析接口返回值
        JsonObject jObject = new JsonParser().parse(response).getAsJsonObject();
        int code = jObject.get("code").getAsInt();
        String msg = jObject.get("msg").getAsString();
        if (code == 200) {
            JsonObject resultObject = jObject.getAsJsonObject("result");
            JsonObject antispam = resultObject.getAsJsonObject("antispam");
            int action = antispam.get("action").getAsInt();
            String taskId = antispam.get("taskId").getAsString();
            JsonArray labelArray = antispam.getAsJsonArray("labels");
            List<Labels> labels = new ArrayList<>();
            for (JsonElement labelElement : labelArray) {
                JsonObject lObject = labelElement.getAsJsonObject();
                int label = lObject.get("label").getAsInt();
                int level = lObject.get("level").getAsInt();
                Labels temp = new Labels();
                temp.setLabel(LabelEnum.fromValue(label));
                temp.setLevel(LevelEnum.fromValue(level));
                labels.add(temp);
            }
            DunResult result = new DunResult();
            result.setTaskId(taskId);
            result.setAction(ActionEnum.fromValue(action));
            result.setLabels(labels);
            return result;
        } else {
            log.error(String.format("易盾检测文本ERROR: code=%s, msg=%s", code, msg));
            return null;
        }
    }

    /**
     * 检查图片
     * @param base64Img base64Img
     * @author shenjun
     * @date 2021/1/23 11:00
     * @return DunResult
     */
    public static DunResult checkImage(String base64Img) {
        Map<String, String> params = new HashMap<>();
        // 1.设置公共参数
        params.put("secretId", SECRETID);
        params.put("businessId", BUSINESSID_IMG);
        params.put("version", "v4");
        params.put("timestamp", String.valueOf(System.currentTimeMillis()));
        params.put("nonce", String.valueOf(new Random().nextInt()));
        // MD5, SM3, SHA1, SHA256
        params.put("signatureMethod", "MD5");
        // 2.设置私有参数
        JsonArray jsonArray = new JsonArray();
        // 传图片base64编码进行检测，name结构产品自行设计，用于唯一定位该图片数据
        JsonObject image = new JsonObject();
        image.addProperty("name", "name");
        image.addProperty("type", 2);
        // 主动回调地址url,如果设置了则走主动回调逻辑
        // image2.addProperty("callbackUrl", "http://***");
        image.addProperty("data", base64Img);
        jsonArray.add(image);

        params.put("images", jsonArray.toString());
        // params.put("account", "java@163.com");
        // params.put("ip", "123.115.77.137");

        // 3.生成签名信息
        String signature = SignatureUtils.genSignature(SECRETKEY, params);
        params.put("signature", signature);

        // 4.发送HTTP请求，这里使用的是HttpClient工具包，产品可自行选择自己熟悉的工具包发送请求
        String response = HttpClient4Utils.sendPost(httpClient, API_URL_IMG, params, Consts.UTF_8);

        // 5.解析接口返回值
        JsonObject resultObject = new JsonParser().parse(response).getAsJsonObject();
        int code = resultObject.get("code").getAsInt();
        String msg = resultObject.get("msg").getAsString();
        if (code == 200) {
            // 图片反垃圾结果
            JsonArray antispamArray = resultObject.getAsJsonArray("antispam");
            //一张张图片进行验证
            JsonElement jsonElement = antispamArray.get(0);
            JsonObject jObject = jsonElement.getAsJsonObject();
            String name = jObject.get("name").getAsString();
            String taskId = jObject.get("taskId").getAsString();
            int status = jObject.get("status").getAsInt();
            // 图片检测状态码，定义为：0：检测成功，610：图片下载失败，620：图片格式错误，630：其它
            if (0 == status) {
                // 图片维度结果
                int action = jObject.get("action").getAsInt();
                JsonArray labelArray = jObject.get("labels").getAsJsonArray();
                // 产品需根据自身需求，自行解析处理，本示例只是简单判断分类级别
                List<Labels> labels = new ArrayList<>();
                for (JsonElement labelElement : labelArray) {
                    JsonObject lObject = labelElement.getAsJsonObject();
                    int label = lObject.get("label").getAsInt();
                    int level = lObject.get("level").getAsInt();

                    Labels temp = new Labels();
                    temp.setLevel(LevelEnum.fromValue(level));
                    temp.setLabel(LabelEnum.fromValue(label));
                    labels.add(temp);
                }
                DunResult result = new DunResult();
                result.setLabels(labels);
                result.setAction(ActionEnum.fromValue(action));
                result.setTaskId(taskId);
                return result;
            } else {
                // status对应失败状态码：610：图片下载失败，620：图片格式错误，630：其它
                log.error(String.format("图片检测失败，taskId=%s，status=%s，name=%s", taskId, status, name));
                return null;
            }
        } else {
            log.error(String.format("ERROR: code=%s, msg=%s", code, msg));
            return null;
        }
    }
}
