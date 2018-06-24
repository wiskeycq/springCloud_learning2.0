package com.cq.controller;

import com.cq.config.GirlConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: caosq
 * @Date: 2018/06/24 17:26
 * @Description:
 */
@RestController
public class GirlController {

    @Autowired
    private GirlConfig girlConfig;

    @GetMapping("/girl/print")
    public String print() {
        return "name = "+girlConfig.getName() + " age = " +girlConfig.getAge();
    }
}
