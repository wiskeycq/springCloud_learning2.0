package com.cq.service;

import com.cq.model.UserInfo;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/28 17:00
 * @Description:
 */
public interface UserInfoService {

    UserInfo findByOpenid(String openid);
}
