package com.yitu.cpcFounding.api.utils.wxCode;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yitu.cpcFounding.api.constant.Configs;
import com.yitu.cpcFounding.api.redis.RedisUtil;
import com.yitu.cpcFounding.api.vo.JsonResult;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取小程序码
 * 
 * @author clj
 * @date 2021-01-25 07:28:47
 */
@Component
public class WXCodeUtil {
	/**
	 * 小程序secret
	 */
	@Value("${wechat.appSecret}")
	private  String SECRET;
	
	/**
	 * 小程序appId
	 */
	@Value("${wechat.appId}")
	private  String APPID;
	
	/**
	 * 获取code的调用接口
	 */
	@Value("${wechat.codeUrl}")
	private  String CODE_URL;
	
	@Autowired
	private RedisUtil redisUtil;
	
	/**
	 * 放进缓存的key
	 */
	private static String CACHE_KEY = "wxCode_access_token";
	
	private static Logger log = LoggerFactory.getLogger(WXCodeUtil.class);

	/**
	 * 获取小程序码图片转成base64
	 * @param param 携带参数
	 * @param page 跳转页
	 * @return_type String
	 * @author 蔡李佳
	 * @date 2021-01-25 08:36:17
	 */
	public JsonResult<Object> getCode(String param, String page) {
		String result;
		try {
			String accessToken = getAccessToken();
			String getParam = CODE_URL + accessToken;
			Map<String, Object> params = new HashMap<>();
			//扫码进入小程序需要携带的参数
			params.put("scene", param);
			if (!StringUtils.isEmpty(page)) {
				//如果不填写这个字段，默认跳主页面
				params.put("page", page);
			}
			//是否需要透明底色，为 true 时，生成透明底色的小程序
			params.put("is_hyaline", true);
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			HttpPost httpPost = new HttpPost(getParam);
			httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
			String body = JSON.toJSONString(params);
			StringEntity entity;
			entity = new StringEntity(body);
			entity.setContentType("image/png");

			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);

			InputStream inputStream = response.getEntity().getContent();
			// 将获取流转为base64格式
			byte[] data;
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			byte[] buff = new byte[100];
			int rc;
			int length = 100;
			while ((rc = inputStream.read(buff, 0, length)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			data = swapStream.toByteArray();
			//错误信息解析
			try {
				String error = new String(data);
				JSONObject json = JSONObject.parseObject(error);
				String errorMsg = json.getString("errmsg");
				if(!StringUtils.isEmpty(errorMsg)){
					return JsonResult.fail(errorMsg);
				}
			}catch (Exception e){
				log.info(e.getMessage());
			}
			result = new String(Base64.getEncoder().encode(data));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return JsonResult.fail(e.getMessage());
		}
		return JsonResult.ok("data:image/jpg;base64," + result);
	}

	/**
	 * 获取accessToken
	 * 
	 * @return_type String
	 * @author 蔡李佳
	 * @date 2021-01-25 05:14:44
	 */
	public  String getAccessToken() {
		String accessToken = (String)redisUtil.get(CACHE_KEY);
		if(StringUtils.isEmpty(accessToken)) {
			String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + APPID + "&secret="
					+ SECRET;
			String accessTokens = HttpUtil.get(url);
			JSONObject parseObject = JSON.parseObject(accessTokens);
			String token = parseObject.getString("access_token");
			 //将token放进缓存
       	 	redisUtil.set(CACHE_KEY, token, Configs.REDIS_KEY_REG_WXCODE_CACHE);
			return token;
		}
		return accessToken;
	}
}
