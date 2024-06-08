package com.yitu.cpcFounding.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 错误控制类
 *
 * @author yanhua
 * @date 2020/9/28 10:08
 */
@Controller
@RequestMapping("/error")
public class ErrorController {
    /**
     * 错误页面
     *
     * @param model Model
     * @param msg   提示信息
     * @return java.lang.String
     * @author yanhua
     * @date 2018/12/12
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(Model model, @RequestParam(value = "msg", required = false) String msg) {
        if (StringUtils.isBlank(msg)) {
            msg = "对不起，系統繁忙，请稍后再试";
        } else {
            msg = "对不起，" + msg;
        }
        model.addAttribute("msg", msg);
        return "error/index";
    }

}
