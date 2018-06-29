package com.cq.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/29 16:47
 * @Description: 跨域配置
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);//是否支持cookie跨域
        config.setAllowedOrigins(Arrays.asList("*"));//允许的原始域，*代表所有；比如http://www.a.com,http://www.b.com
        config.setAllowedMethods(Arrays.asList("*"));//允许的请求方式，比如post，get，put等
        config.setAllowedHeaders(Arrays.asList("*"));//允许的请求头
        config.setMaxAge(300l);//缓存时间，在这个时间内，对于相同的跨域请求，不会进行检查

        source.registerCorsConfiguration("/**",config);// 第一个参数代表在哪些域名下进行配置，"/**"代表所有
        return new CorsFilter(source);
    }
}
