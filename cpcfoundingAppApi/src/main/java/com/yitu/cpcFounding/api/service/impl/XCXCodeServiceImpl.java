package com.yitu.cpcFounding.api.service.impl;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yitu.cpcFounding.api.constant.Configs;
import com.yitu.cpcFounding.api.domain.DangHistory;
import com.yitu.cpcFounding.api.enums.*;
import com.yitu.cpcFounding.api.mapper.AdMapper;
import com.yitu.cpcFounding.api.mapper.DangHistoryMapper;
import com.yitu.cpcFounding.api.redis.RedisUtil;
import com.yitu.cpcFounding.api.service.UserScoreService;
import com.yitu.cpcFounding.api.service.XCXCodeService;
import com.yitu.cpcFounding.api.utils.EnumUtil;
import com.yitu.cpcFounding.api.utils.HttpUtil;
import com.yitu.cpcFounding.api.utils.OkHttpClientUtil;
import com.yitu.cpcFounding.api.utils.wxCode.WXCodeUtil;
import com.yitu.cpcFounding.api.vo.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.yitu.cpcFounding.api.vo.ad.WXCodeVO;

/**
 * 获取小程序码业务层
 *
 * @author clj
 * @date 2021-02-02 04:26:40
 */
@Service
@Slf4j
public class XCXCodeServiceImpl implements XCXCodeService {
    @Autowired
    private AdMapper adMapper;
    @Autowired
    private WXCodeUtil wXCodeUtil;
    @Autowired
    private RedisUtil redisUtil;
    @Value("${wechat.codeUrl}")
    private String codeUrl;
    @Value("${wechat.accessTokenUrl}")
    private String accessTokenUrl;
    @Value("${wechat.appSecret}")
    private String appSecret;
    @Value("${wechat.appId}")
    private String appId;
    @Resource
    private OkHttpClientUtil okHttpClientUtil;
    @Autowired
    private DangHistoryMapper dangHistoryMapper;

    /**
     * 获取小程序码以外的相关信息
     *
     * @param request
     * @param type     类型
     * @param param    参数
     * @param flag     是否传用户id
     * @param detailId 详情id
     * @return com.yitu.cpcFounding.api.vo.JsonResult<com.yitu.cpcFounding.api.vo.ad.WXCodeVO>
     * @author qixinyi
     * @date 2021/6/18 15:40
     */
    @Override
    public JsonResult<WXCodeVO> getWXCodeInfo(HttpServletRequest request, Integer type, String param, Boolean flag,
                                              Long detailId) {
        // 封装用户id
        if (flag == null || !flag) {
            param = "uid=0";
        } else {
            param = "uid=" + param;
        }

        // 封装详情id
        if (detailId == null) {
            param += "&id=";
        } else {
            param += "&id=" + detailId;
        }

        // 校验
        if (type.equals(AdTypeEnum.ARTICLE_SHARE.getValue()) && detailId == 0) {
            return JsonResult.fail("请输入详情id");
        }

        WXCodeVO wxCodeVO = new WXCodeVO();
        List<WXCodeVO> wxCodeInfo = adMapper.getWXCodeInfo(type);
        // 随机取得数据
        if (wxCodeInfo!=null && wxCodeInfo.size() > 0) {
            Random random = new Random();
            int index = random.nextInt(wxCodeInfo.size());
            wxCodeVO = wxCodeInfo.get(index);

            if (type.equals(AdTypeEnum.ARTICLE_SHARE.getValue())) {
                // 如果为文章分享 封装文章标题
                DangHistory dangHistory = dangHistoryMapper.selectById(detailId);
                if (dangHistory != null) {
                    String title = wxCodeVO.getTitle();
                    wxCodeVO.setTitle(title + "《" + dangHistory.getTitle() + "》");
                }

                // 封装图片地址
                Integer coverType = dangHistory.getCoverType();
                if (coverType!=null && coverType != DangHistoryConverTypeEnum.NO_PIC.getValue()) {
                    // 取第一张图
                    String url = dangHistory.getUrl();
                    String firstUrl = url.split(",")[0];

                    // 处理图片路径
                    wxCodeVO.setPath(HttpUtil.getWebResourcePath(firstUrl));
                } else {
                    if (!StringUtils.isEmpty(wxCodeVO.getPath())) {
                        wxCodeVO.setPath(HttpUtil.getNetResourcePath(request, wxCodeVO.getPath()));
                    }
                }
            } else if (type != AdShareTypeEnum.ARTICLE_SHARE.getValue() && !StringUtils.isEmpty(wxCodeVO.getPath())) {
                wxCodeVO.setPath(HttpUtil.getNetResourcePath(request, wxCodeVO.getPath()));
            }

            wxCodeVO.setUserId(param);
        }
        return JsonResult.ok(wxCodeVO);
    }

    /**
     * 获取小程序码
     *
     * @param param    参数
     * @param param    携带参数
     * @param flag     是否传用户id
     * @param detailId 详情id
     * @return com.yitu.partyday.api.vo.JsonResult<java.lang.Object>
     * @author qixinyi
     * @date 2021/6/9 9:17
     */
    @Override
    public JsonResult<Object> getCode(String param, String page, Boolean flag,
                                      Long detailId) {
        // 封装用户id
        if (flag == null || !flag) {
            param = "uid=0";
        } else {
            param = "uid=" + param;
        }

        // 封装详情id
        if (detailId == null) {
            param += "&id=";
        } else {
            param += "&id=" + detailId;
        }

        // 获取小程序码
        return getCode(param, page);
    }

    /**
     * 获取小程序码图片转成base64
     * 文档详见：https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/qr-code/wxacode.getUnlimited.html
     *
     * @param param 页面参数
     * @param page  页面地址
     * @return com.yitu.zbcard.api.vo.JsonResult<java.lang.Object>
     * @author qinmingtong
     * @date 2021/3/9 11:28
     */
    public JsonResult<Object> getCode(String param, String page) {
        String result;
        try {
            String accessToken = getAccessToken();
            String getParam = codeUrl + accessToken;
            Map<String, Object> params = new HashMap<>();
            //扫码进入小程序需要携带的参数
            params.put("scene", param);
            if (!org.springframework.util.StringUtils.isEmpty(page)) {
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
                Integer errcode = (Integer) json.get("errcode");

                if (errcode == WxSecCheckEnum.TOKEN_ERRCODE.getValue()) {
                    cleanAccessToken();
                    getCode(param, page);
                }

                if (!org.springframework.util.StringUtils.isEmpty(errorMsg)) {
                    return JsonResult.fail(errorMsg);
                }
            } catch (Exception e) {
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
     * @return java.lang.String
     * @author pangshihe
     * @date 2021/6/11 15:25
     */
    public String getAccessToken() {
        String accessToken = (String) redisUtil.get(Configs.CACHE_KEY);
        if (org.springframework.util.StringUtils.isEmpty(accessToken)) {
            String url = accessTokenUrl + "grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
            String accessTokens = okHttpClientUtil.httpGet(url);
            JSONObject parseObject = JSON.parseObject(accessTokens);
            String token = parseObject.getString("access_token");
            //将token放进缓存
            redisUtil.set(Configs.CACHE_KEY, token, Configs.REDIS_KEY_REG_WXCODE_CACHE);
            return token;
        }
        return accessToken;
    }

    /**
     * 删除accessToken缓存
     *
     * @return void
     * @author pangshihe
     * @date 2021/6/4 11:00
     */
    public void cleanAccessToken() {
        redisUtil.del(Configs.CACHE_KEY);
    }
}
