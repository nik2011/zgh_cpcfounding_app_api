package com.yitu.cpcFounding.api.config;

import com.yitu.cpcFounding.api.filter.RefererFilter;
import com.yitu.cpcFounding.api.filter.XssAndSqlFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器配置
 * @author yaoyanhua
 * @version 1.0
 * @date 2020/6/18
 */
@Configuration
public class FilterConfig {

    /**
     * RefererFilter
     * @param
     * @return org.springframework.boot.web.servlet.FilterRegistrationBean
     * @author yaoyanhua
     * @date 2020/6/23 11:41
     */
    @Bean
    public FilterRegistrationBean RefererFilterRegistration(){
        FilterRegistrationBean<RefererFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new RefererFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("RefererFilter");
        filterRegistrationBean.setOrder(1);
        return filterRegistrationBean;
    }

    /**
     * XssAndSqlFilter
     * @param
     * @return org.springframework.boot.web.servlet.FilterRegistrationBean
     * @author yaoyanhua
     * @date 2020/6/23 11:41
     */
    @Bean
    public FilterRegistrationBean XssAndSqlFilterRegistration(){
        FilterRegistrationBean<XssAndSqlFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new XssAndSqlFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setName("XssAndSqlFilter");
        filterRegistrationBean.setOrder(2);
        return filterRegistrationBean;
    }
}
