package com.cq.filter;

import com.cq.constant.RedisConstant;
import com.cq.utils.CookieUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_DECORATION_FILTER_ORDER;
import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.PRE_TYPE;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/28 13:48
 * @Description: 买家鉴权过滤[买家和卖家分开以便于分别鉴权控制，不影响其他角色]
 */
@Component
public class AuthBuyerFilter extends ZuulFilter {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String filterType() {
        return PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return PRE_DECORATION_FILTER_ORDER - 1;//减1，相当于优先级在PRE_DECORATION_FILTER_ORDER之前
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        if("/order/order/create".equals(request.getRequestURI())) {
            return true;//true代表过滤器生效，false代表不生效
        }
        return false;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        /**
         * /order/create 只能买家访问(cookie里面有openid)
         */
        Cookie cookie = CookieUtil.get(request, "openid");
        if (cookie == null || StringUtils.isEmpty(cookie.getValue())) {
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
    /**
     * 生产权限一般都是放在数据库的，所以网关做鉴权的时候，要从数据库中去哪数据进行判断，所以user服务需要给网关服务提供
     * 接口；但每次调用user服务从数据库读取性能太差，所以要用redis；第一步，先从redis读取权限信息，如果有，则判断是否有
     * 权限；如果redis没有，则调用user服务从数据库查数据，若用户信息变更（插入或者更新）时，在变更的地方，也同时通过mq
     * 发送一个消息，异步接收到消息后，将数据写入redis
     *
     * 在微服务架构下，每个微服务访问都需要鉴权。
     * 分布式session，将用户认证的信息存储在共享储存中
     */
}
