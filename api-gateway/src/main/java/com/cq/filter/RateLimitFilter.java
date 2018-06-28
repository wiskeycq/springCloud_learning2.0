package com.cq.filter;

import com.cq.exception.RateLimitException;
import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVLET_DETECTION_FILTER_ORDER;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/28 14:26
 * @Description: 前置(pre) 限流拦截器【因为需要在请求转发之前调用，所以应该在鉴权拦截之前】
 *  限流用令牌桶算法(google有开源组件，guava组件里面就有令牌桶算法的实现)
 */

@Component
public class RateLimitFilter extends ZuulFilter{

    //每秒中往里面放100个令牌
    private static final RateLimiter rateLimiter = RateLimiter.create(100);

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        //限流是所有优先级最高的；FilterConstants中值越小优先级越高
        return SERVLET_DETECTION_FILTER_ORDER -1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //如果没获取到令牌则抛出一个异常
        if (!rateLimiter.tryAcquire()) {
            throw new RateLimitException();
        }
        return null;
    }
}
