package com.yitu.cpcFounding.api.controller;

import com.yitu.cpcFounding.api.redis.RedisUtil;
import com.yitu.cpcFounding.api.vo.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * HealthController
 * @author yaoyanhua
 * @date 2020/4/27
 */
@RestController
@RequestMapping("health")
public class HealthController {

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 健康检查
     * @param
     * @return java.lang.Integer
     * @author yaoyanhua
     * @date 2020/6/23 11:52
     */
    @GetMapping("status")
    public Integer status() {
        return 1;
    }

    /**
     * 测试部署
     * @param
     * @author shenjun
     * @date 2021/2/1 15:15
     * @return java.lang.String
     */
    @GetMapping("update")
    public String update() {
        return "2021-2-25 14:35:37";
    }

    @GetMapping("statusTest")
    @ResponseBody
    public JsonResult statusTest(@RequestParam(value = "certType", defaultValue = "0") int certType,
                                 @RequestParam(value = "certNo", defaultValue = "") String certNo) {
        redisUtil.set("test2", "123456");
        String aa = (String)redisUtil.get("test2");
        return JsonResult.ok(aa);
    }
}
