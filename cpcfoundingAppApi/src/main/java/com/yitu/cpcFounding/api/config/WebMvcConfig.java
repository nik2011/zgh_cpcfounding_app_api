package com.yitu.cpcFounding.api.config;

import com.yitu.cpcFounding.api.domain.FileConfigEntity;
import com.yitu.cpcFounding.api.interceptors.AntiBrushIntercept;
import com.yitu.cpcFounding.api.interceptors.AuthenticationHandler;
import com.yitu.cpcFounding.api.auth.UserIdArgumentsResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.List;

/**
 * 登录验证和权限验证拦截器配置
 * 注释掉@Configuration关闭配置
 *
 * @author pangshihe
 * @date 2020/6/22 14:12
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private FileConfigEntity fileConfigEntity;
    @Autowired
    private AntiBrushIntercept antiBrushIntercept;


    /**
     * 注入认证拦截类到spring容器
     * @return
     */
    @Bean
    public AuthenticationHandler getAuthenticationHandler(){
        return new AuthenticationHandler();
    }

    /**
     * 注入参数解决到spring容器
     * @return
     */
    @Bean
    public UserIdArgumentsResolver getUserIdArgumentsResolver(){
        return new UserIdArgumentsResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(getAuthenticationHandler())
                //添加需要验证登录用户操作权限的请求
                .addPathPatterns("/**");
        registry.addInterceptor(antiBrushIntercept).addPathPatterns("/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(getUserIdArgumentsResolver());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        if (fileConfigEntity.getMappingSwitch()) {
            //配置server虚拟路径，handler为jsp中访问的目录，locations为files相对应的本地路径
            registry.addResourceHandler("/files/**").addResourceLocations("file:/" + fileConfigEntity.getUrl());
        }
    }
}