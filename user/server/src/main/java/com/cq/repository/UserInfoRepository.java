package com.cq.repository;

import com.cq.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/28 16:57
 * @Description:
 */
public interface UserInfoRepository  extends JpaRepository<UserInfo,String> {

    UserInfo findByOpenid(String openid);
}
