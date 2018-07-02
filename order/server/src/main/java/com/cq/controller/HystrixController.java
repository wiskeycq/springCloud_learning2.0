package com.cq.controller;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

/**
 * @Auther: caoqsq
 * @Date: 2018/7/2 09:46
 * @Description:
 */
@RestController
@DefaultProperties(defaultFallback = "defaultFallback")
public class HystrixController {

    //@HystrixCommand(fallbackMethod = "fallback")
    //@HystrixCommand //使用默认提示的话后面不需要指定方法
   /* @HystrixCommand(commandProperties = {
            @HystrixProperty(name ="execution.isolation.thread.timeoutInMilliseconds",value="3000")
    }) //超时设置；默认的请求超时时间为1s，这里修改到3s，超时时间设置根据具体业务来设置（HystrixCommandProperties）*/
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name="circuitBreaker.enabled",value = "true"), //是否设置熔断,默认是true
            @HystrixProperty(name="circuitBreaker.requestVolumeThreshold",value = "10"),//当在配置时间窗口内达到此数量的失败后，进行短路。默认20个
            @HystrixProperty(name="circuitBreaker.sleepWindowInMilliseconds",value = "10000"),//短路多久以后开始尝试是否恢复，默认5s
            @HystrixProperty(name="circuitBreaker.errorThresholdPercentage",value = "60")//出错百分比阈值，当达到此阈值后，开始短路。默认50%
    }) //服务熔断
    //@HystrixCommand
    @GetMapping("/getProductInfoList")
    public String getProductInfoList(@RequestParam("number") Integer number) {
        //加入number是为了测试熔断
       if (number % 2 == 1) {
           return "success";
       }
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.postForObject("http://localhost:7001/product/listForOrder",
                Arrays.asList("157875227953464068"),String.class);
        return response;
        //throw new RuntimeException("订单服务发生异常了");
    }

    public String fallback() {
        return "太拥挤了,请稍后再试!";
    }

    public String defaultFallback() {
        return "默认提示: 太拥挤了,请稍后再试!";
    }

    /** 服务降级
     *  1.调用的服务端发生异常了，可以用Hystrix这种加HystrixCommand注解的方式实现服务降级，并且控制台也不会报错。
     *  2.直接抛一个异常的情况，也可以使用这种方式触发服务降级。使用的场景有：比如数据库连接太多，并发数太多等情况。
     *  3.如果降级的地方很多，就需要写很多这样的fallback()方法，比较麻烦，HyStrix中提供了默认的提示
     *
     *  依赖隔离
     *  依赖隔离（线程池隔离），会为每个HystrixCommand创建一个独立的线程池，此服务请求响应慢或者不可用，只会对自己产生影响，
     *  不会影响其他服务，调用了HystrixCommand注解，hystrix自动实现了依赖隔离，所以依赖隔离和服务降级是一体化实现的。
     *
     *  服务熔断
     */
}
