package com.yitu.cpcFounding.api.controller.page;

import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 首页
 * @author yaoyanhua
 * @version 1.0
 * @date 2020/6/13
 */
@Controller
@Api(tags = "首页控制器", hidden = true)
public class HomeController {

    /** 
     * 没有权限页面
     * @param 
     * @return java.lang.String
     * @author yaoyanhua
     * @date 2020/6/23 11:51
     */
    @GetMapping("/noPermission")
    public String noPermission(){
        return "noPermission";
    }

}
