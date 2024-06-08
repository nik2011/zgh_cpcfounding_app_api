package com.yitu.cpcFounding.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动入口
 * @author yaoyanhua
 * @version 1.0
 * @date 2020/6/11
 */
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
@MapperScan(basePackages = "com.yitu.cpcFounding.api.mapper")
public class CPCFoundingAppApiApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(CPCFoundingAppApiApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CPCFoundingAppApiApplication.class);
    }

}
