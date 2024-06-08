package com.yitu.cpcFounding.api.exception;

import com.alibaba.fastjson.JSONObject;
import com.yitu.cpcFounding.api.enums.ResponseCode;
import com.yitu.cpcFounding.api.utils.HttpUtil;
import com.yitu.cpcFounding.api.vo.JsonResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 全局异常类
 * @author yaoyanhua
 * @version 1.0
 * @date 2020/6/11
 */
@ControllerAdvice
public class GlobalException {
    private static final Pattern FILTER_EXCEPTION1_PATTERN = Pattern.compile("request method \'get\' not supported", Pattern.CASE_INSENSITIVE);
    private static Logger log = LoggerFactory.getLogger(GlobalException.class);

    private static List<Pattern> filterPatternList;
    private static synchronized List<Pattern> getFilterPatternList() {
        if(filterPatternList == null) {
            filterPatternList = new ArrayList<>();
            filterPatternList.add(FILTER_EXCEPTION1_PATTERN);
        }
        return filterPatternList;
    }

    /**
     * 登录认证异常
     * @param ex
     * @return JsonResult
     * @author yaoyanhua
     * @date 2020/6/23 11:56
     */
    @ResponseBody
    @ExceptionHandler({UnauthenticatedException.class, AuthenticationException.class})
    public JsonResult authenticationException(AuthorizationException ex) {
        log.info(ex.getMessage() + ResponseCode.UNAUTHORIZED.getMessage(), ex);
        return JsonResult.build(ResponseCode.UNAUTHORIZED, "没有登录");
    }

    /**
     * 权限异常
     * @param ex
     * @param request
     * @param response
     * @return JsonResult
     * @author yaoyanhua
     * @date 2020/6/23 11:56
     */
    @ResponseBody
    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class})
    public JsonResult authorizationException(AuthorizationException ex, HttpServletRequest request, HttpServletResponse response) {
        log.info(ex.getMessage() + ResponseCode.UNAUTHORIZED.getMessage(), ex);
        if(HttpUtil.isAjaxRequest(request)){
            return JsonResult.build(ResponseCode.NOT_PERMISSION, "没有权限");
        }
        try {
            response.sendRedirect(request.getContextPath() + "/noPermission");
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 自定义异常
     * @param e
     * @return JsonResult
     * @author yaoyanhua
     * @date 2020/6/23 11:56
     */
    @ResponseBody
    @ExceptionHandler(ConsciousException.class)
    public JsonResult handleConsciousException(ConsciousException e) {
        log.info(e.getMessage(), e);
        return JsonResult.build(e.getCode(), e.getLocalizedMessage());
    }

    /**
     * 其他异常
     * @param ex
     * @param requset
     * @param response
     * @return void
     * @author yaoyanhua
     * @date 2020/6/23 11:56
     */
    @ExceptionHandler(Exception.class)
    public void handleException(Exception ex, HttpServletRequest requset, HttpServletResponse response) throws IOException {
        String message = ex.getLocalizedMessage();
        if(message == null || "".equals(message)){
            message = ex.getClass().getName();
        }
//        if(!isFilterException(message)){
//            log.error(ex.getMessage(), ex);
//        }
        log.error(ex.getMessage(), ex);

        log.error("系统异常，异常信息:{}",ex.toString());
        message = "系统繁忙，请稍后再试";
        //直接返回json,ie浏览器会提示下载
        if(HttpUtil.isAjaxRequest(requset)){
            response.setContentType("application/json;charset=UTF-8");
        }
        else{
            response.setContentType("text/html;charset=utf-8");
        }
        response.getWriter().print(JSONObject.toJSON(JsonResult.build(ResponseCode.INTERNAL_SERVER_ERROR, message, null)));
    }

    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public JsonResult methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, HttpServletResponse response){
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        List<String> errorMsgArr = new ArrayList<>();
        errors.forEach(e -> errorMsgArr.add(e.getDefaultMessage()));
        String errorMsg = StringUtils.join(errorMsgArr, ",");
        log.error(ex.getLocalizedMessage(), ex);
        return JsonResult.fail(errorMsg);
    }

    /**
     * 验证保存的值不合格
     *
     * @param cve cve
     * @return com.sihc.productionsafety.utils.JsonResult<java.lang.String>
     * @author zhujinming
     * @date 9:29 20/06/29
     */
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseBody
    public JsonResult<String> handleMethodArgumentNotValidException(ConstraintViolationException cve) {
        List<String> msgList = new ArrayList<>();
        for (ConstraintViolation<?> constraintViolation : cve.getConstraintViolations()) {
            msgList.add(constraintViolation.getMessage());
        }
        String messages = org.apache.commons.lang3.StringUtils.join(msgList.toArray(), ";");
        return JsonResult.build(ResponseCode.UNVALIDATED, messages);
    }

    /**
     * 是否过滤的异常，不记录到日志
     * @param err
     * @return boolean
     * @author yaoyanhua
     * @date 2020/6/23 11:56
     */
    private boolean isFilterException(String err) {
        if(StringUtils.isBlank(err)){
            return true;
        }
        if(getFilterPatternList().stream().anyMatch(p -> p.matcher(err).find())){
            return true;
        }
        return false;
    }
}
