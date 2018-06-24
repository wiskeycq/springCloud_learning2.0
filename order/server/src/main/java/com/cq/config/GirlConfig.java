package com.cq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Auther: caosq
 * @Date: 2018/06/24 17:24
 * @Description: 通过配置文件的形式从config里面获取配置
 */
@Data
@Component
@ConfigurationProperties(prefix="girl")
@RefreshScope
public class GirlConfig {

    private String name;

    private Integer age;
}
