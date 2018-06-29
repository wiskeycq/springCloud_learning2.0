package com.cq.service;

import com.cq.DTO.OrderDTO;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/21 10:41
 * @Description:
 */
public interface OrderService {

    /**
     * 创建订单（只能买家才能操作）
     * @param orderDTO
     * @return
     */
    OrderDTO create(OrderDTO orderDTO);

    /**
     * 完结订单(只能卖家才能操作）
     */
    OrderDTO finish(String orderId);
}
