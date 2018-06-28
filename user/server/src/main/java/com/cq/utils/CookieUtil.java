package com.cq.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/28 17:28
 * @Description:
 */
public class CookieUtil {

    public static void set(HttpServletResponse response,String name,String value,int maxAge) {
        Cookie cookie = new Cookie(name,value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }
}
