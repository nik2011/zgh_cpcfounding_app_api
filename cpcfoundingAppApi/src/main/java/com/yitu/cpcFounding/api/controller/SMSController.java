package com.yitu.cpcFounding.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.yitu.cpcFounding.api.service.SMSSevice;
import com.yitu.cpcFounding.api.utils.OkHttpClientUtil;
import com.yitu.cpcFounding.api.utils.RestSignUtil;
import com.yitu.cpcFounding.api.vo.JsonResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信服务控制
 * 2021/1/26 10:00
 *
 * @author luzhichao
 **/
@RestController
@RequestMapping("/api/sms")
@Api(value = "短信服务控制", tags = {"短信服务控制"})
public class SMSController {
    @Value("${rest.appid}")
    private String mAppId;
    @Value("${rest.appsecret}")
    private String mAppSecret;
    @Value("${rest.url}")
    private String mRestUrl;

    @Autowired
    SMSSevice lsSMSSevice;

    /**
     * @description: 获取短信验证码
     * @return: JsonResult
     * @author: luzhichao
     * @date: 2021/1/22 14:45
     */
    @PostMapping("/getSMSVerifiCode")
    @ApiOperation(value = "获取短信验证码")
    @ApiOperationSupport(author = "luzhichao")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "int", required = true)
    })
    public JsonResult sendMsg(@RequestParam("phone") String phone) {
        return lsSMSSevice.sendVerifiCode(phone);
    }

    /**
     * @description: 校验验证码
     * @return: JsonResult
     * @author: luzhichao
     * @date: 2021/1/22 14:45
     */
    @PostMapping("/verifiCode")
    @ApiOperation(value = "校验验证码")
    @ApiOperationSupport(author = "luzhichao")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号", dataType = "int", required = true),
            @ApiImplicitParam(name = "code", value = "验证码", dataType = "int", required = true)
    })
    public JsonResult verifiCode(@RequestParam("phone") String phone, @RequestParam("code") String code) {
//        return lsSMSSevice.verifiCode(phone, code);

        return JsonResult.ok();
    }

    /**
     * @description: test
     * @return: JsonResult
     * @author: luzhichao
     * @date: 2021/1/22 14:45
     */
    @PostMapping("/test")
    @ApiOperation(value = "test")
    @ApiOperationSupport(author = "test")
    public JsonResult test() {
        Map params = new HashMap();
        //添加基本参数
        addBasicParams(params);
        params.put("name", "nik");
        String paramStr = RestSignUtil.mapToUrlString(params);
        String signedStr = RestSignUtil.sign(paramStr, "MD5", mAppSecret);
        params.put("signature", signedStr);
        OkHttpClientUtil httpClient = new OkHttpClientUtil();
        String url = mRestUrl + "/sms/status";
        String result = httpClient.httpPost(url, params, 2);
        if (result == null) {
            return JsonResult.fail("短信接口请求失败");
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        if (jsonObject == null) {
            return JsonResult.fail("短信接口请求失败");
        }
        long status = Long.valueOf(jsonObject.getString("status"));
        if (status != 200) {
            return JsonResult.fail(jsonObject.getString("msg"));
        }
        return JsonResult.ok();
    }


    /**
     * @param
     * @return
     * @Description: 添加基本参数
     * @author yanhua
     * @date 2019/1/7
     */
    private void addBasicParams(Map<String, String> param) {
        param.put("appid", mAppId);
        param.put("timeStamp", String.valueOf(System.currentTimeMillis()));
        param.put("signType", "MD5");
    }

}
