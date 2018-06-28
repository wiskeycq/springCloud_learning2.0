package com.cq.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/28 16:49
 * @Description:
 */
@Data
@Entity
public class UserInfo {
    /** 用户id. */
    @Id
    private String id;

    /** 用户名字. */
    private String userName;

    /** 用户密码. */
    private String password;

    /** 微信openid. */
    private String openid;

    /** 角色 1买家2卖家 */
    private Integer role;

    /** 创建时间. */
    private Date createTime;

    /** 更新时间. */
    private Date updateTime;
}
