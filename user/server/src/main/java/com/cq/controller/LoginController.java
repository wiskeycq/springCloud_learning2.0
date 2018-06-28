package com.cq.controller;

import com.cq.VO.ResultVO;
import com.cq.constant.CookieConstant;
import com.cq.enums.ResultEnum;
import com.cq.enums.RoleEnum;
import com.cq.model.UserInfo;
import com.cq.service.UserInfoService;
import com.cq.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

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

    /**
     * 买家登录
     */
    @GetMapping("/buyer")
    public ResultVO buyer(@RequestParam("openid") String openid, HttpServletResponse response) {
        //1.openId和数据库里的数据匹配
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
    public ResultVO seller(@RequestParam("openId") String openId, HttpServletResponse response) {
        return null;
    }
}
