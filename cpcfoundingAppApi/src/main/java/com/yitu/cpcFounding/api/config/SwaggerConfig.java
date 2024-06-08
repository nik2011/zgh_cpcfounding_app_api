package com.yitu.cpcFounding.api.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.collect.Lists;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;


/**
 * SwaggerConfig
 * @author yaoyanhua
 * @version 1.0
 * @date 2020/6/11
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@ConditionalOnProperty(value = {"knife4j.enable"}, matchIfMissing = true)
public class SwaggerConfig {

    /**
     * 默认api
     * @param
     * @return springfox.documentation.spring.web.plugins.Docket
     * @author yaoyanhua
     * @date 2020/6/23 11:49
     */
    @Bean(value = "defaultApi")
    public Docket defaultApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //.groupName("默认接口")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.yitu.cpcFounding.api"))
                //只有标记了@ApiOperation的方法才会暴露出给swagger
                //.apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(Lists.newArrayList(securityContext())).securitySchemes(Lists.<SecurityScheme>newArrayList(apiKey()));
    }

    /**
     * 构建apiInfo
     * @param
     * @return springfox.documentation.service.ApiInfo
     * @author yaoyanhua
     * @date 2020/6/23 11:50
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("七一活动系统小程序接口文档")
                .description("七一活动系统小程序接口文档")
                .termsOfServiceUrl("http://doc.cloud.etop.com")
                .contact("七一活动系统")
                .version("1.0")
                .build();
    }

    /**
     * apikey
     * @param
     * @return springfox.documentation.service.ApiKey
     * @author yaoyanhua
     * @date 2020/6/23 11:50
     */
    private ApiKey apiKey() {
        return new ApiKey("Token", "Token", "header");
    }

    /**
     * securityContext
     * @param
     * @return springfox.documentation.spi.service.contexts.SecurityContext
     * @author yaoyanhua
     * @date 2020/6/23 11:50
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex("/.*"))
                .build();
    }

    /**
     * defaultAuth
     * @param
     * @return java.util.List<springfox.documentation.service.SecurityReference>
     * @author yaoyanhua
     * @date 2020/6/23 11:50
     */
    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Lists.newArrayList(new SecurityReference("Token", authorizationScopes));
    }
}
