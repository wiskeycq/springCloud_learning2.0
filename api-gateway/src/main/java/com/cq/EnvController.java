package com.cq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: caosq
 * @Date: 2018/06/24 10:26
 * @Description:
 */
/*@RestController
@RequestMapping("/env")
@RefreshScope
public class EnvController {

    @Value("${zuul.routes.myProductA.path}")
    private String env;

    @GetMapping("/print")
    public String print() {
        return env;
    }
}*/
