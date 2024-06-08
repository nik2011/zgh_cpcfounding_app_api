package com.yitu.cpcFounding.api.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MybatisPlusConfig
 * @author yaoyanhua
 * @version 1.0
 * @date 2020/6/11
 */
@EnableTransactionManagement
@Configuration
@MapperScan("com.yitu.cpcFounding.api.mapper")
public class MybatisPlusConfig {

    /**
     * 分页插件
     * @param
     * @return com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor
     * @author yaoyanhua
     * @date 2020/6/23 11:41
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

}
