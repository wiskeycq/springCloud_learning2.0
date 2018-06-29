package com.cq.controller;

import com.cq.VO.ResultVO;
import com.cq.constant.CookieConstant;
import com.cq.constant.RedisConstant;
import com.cq.enums.ResultEnum;
import com.cq.enums.RoleEnum;
import com.cq.model.UserInfo;
import com.cq.service.UserInfoService;
import com.cq.utils.CookieUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/28 17:03
 * @Description:
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 买家登录
     */
    @GetMapping("/buyer")
    public ResultVO buyer(@RequestParam("openid") String openid, HttpServletResponse response) {
        //1.openid和数据库里的数据匹配
        UserInfo userInfo = userInfoService.findByOpenid(openid);
        if (userInfo == null) {
            return ResultVO.error(ResultEnum.LOGIN_FAIL);
        }
        //2.判断角色
        if (RoleEnum.BUYER.getCode() != userInfo.getRole()) {
            return ResultVO.error(ResultEnum.ROLE_ERROR);
        }
        //3.cookie里设置openId=abc
        CookieUtil.set(response, CookieConstant.OPENID,openid,CookieConstant.EXPIRE);

        return ResultVO.success();
    }

    /**
     * 卖家登录
     */
    @GetMapping("/seller")
    public ResultVO seller(@RequestParam("openid") String openid, HttpServletRequest request, HttpServletResponse response) {

        //判断是否登录(避免不停的往redis中写数据)
        Cookie cookie = CookieUtil.get(request,CookieConstant.TOKEN);
        //cookie不为空且redis中有值，则已经登录
        if (cookie != null) {
            String redisValue = stringRedisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_TEMPLATE,cookie.getValue()));
            if (StringUtils.isNotEmpty(redisValue)) {
                return ResultVO.success();
            }
        }

        //1.openId和数据库里的数据匹配
        UserInfo userInfo = userInfoService.findByOpenid(openid);
        if (userInfo == null) {
            return ResultVO.error(ResultEnum.LOGIN_FAIL);
        }
        //2.判断角色
        if (RoleEnum.SELLER.getCode() != userInfo.getRole()) {
            return ResultVO.error(ResultEnum.ROLE_ERROR);
        }

        //3.redis设置key=UUID, value=xyz`(程序一般是先从cookie里面去找，如果找不到再去redis里面找，所以redis设置应该放在cookie设置之前)
        //redis设置过期时间跟Cookie一样
        String  uuid= UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(
                String.format(RedisConstant.TOKEN_TEMPLATE,uuid),
                openid,
                CookieConstant.EXPIRE,
                TimeUnit.SECONDS);

        //4.cookie里设置token=UUID
        CookieUtil.set(response, CookieConstant.TOKEN, uuid, CookieConstant.EXPIRE);

        return ResultVO.success();
    }
}
