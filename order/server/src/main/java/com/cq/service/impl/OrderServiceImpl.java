package com.cq.service.impl;

import com.cq.DTO.OrderDTO;
import com.cq.enums.OrderStatusEnum;
import com.cq.enums.PayStatusEnum;
import com.cq.enums.ResultEnum;
import com.cq.exception.OrderException;
import com.cq.model.*;
import com.cq.product.client.ProductClient;
import com.cq.repository.OrderDetailRepository;
import com.cq.repository.OrderMasterRepository;
import com.cq.service.OrderService;
import com.cq.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/21 10:41
 * @Description:
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductClient productClient;

    /**
     * 1. 查询商品信息(调用商品服务)
     * 2. 计算总价
     * 3. 扣库存(调用商品服务)
     * 4. 订单入库
     */
    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {

        String orderId = KeyUtil.genUniqueKey();

        //1. 查询商品信息(调用商品服务)
        List<String> productIdList = orderDTO.getOrderDetailList().stream()
                .map(OrderDetail::getProductId).collect(Collectors.toList());
        List<ProductInfoOutput> productInfoList = productClient.listForOrder(productIdList);

        //计算总价
        BigDecimal orderAmout = new BigDecimal(BigInteger.ZERO);
        for (OrderDetail orderDetail: orderDTO.getOrderDetailList()) {
            for (ProductInfoOutput productInfo: productInfoList) {
                if (productInfo.getProductId().equals(orderDetail.getProductId())) {
                    //单价*数量
                    orderAmout = productInfo.getProductPrice()
                            .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                            .add(orderAmout);
                    BeanUtils.copyProperties(productInfo, orderDetail);
                    orderDetail.setOrderId(orderId);
                    orderDetail.setDetailId(KeyUtil.genUniqueKey());
                    //订单详情入库
                    orderDetailRepository.save(orderDetail);
                }
            }
        }

        //扣库存(调用商品服务)
        List<DecreaseStockInput> decreaseStockInputList = orderDTO.getOrderDetailList().stream()
                .map(e -> new DecreaseStockInput(e.getProductId(), e.getProductQuantity()))
                .collect(Collectors.toList());
        productClient.decreaseStock(decreaseStockInputList);

        //订单入库
        OrderMaster orderMaster = new OrderMaster();
        orderDTO.setOrderId(orderId);
        BeanUtils.copyProperties(orderDTO, orderMaster);
        orderMaster.setOrderAmount(orderAmout);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW.getCode());
        orderMaster.setPayStatus(PayStatusEnum.WAIT.getCode());
        orderMasterRepository.save(orderMaster);
        return orderDTO;
    }

    /**
     * 买家下单，卖家接单，并把订单状态置为完结状态
     */
    @Override
    @Transactional
    public OrderDTO finish(String orderId) {
        //1.查询订单
        Optional<OrderMaster> orderMasterOptional = orderMasterRepository.findById(orderId);
        if (!orderMasterOptional.isPresent()) {
            throw new OrderException(ResultEnum.ORDER_NOT_EXIST);
        }
        //2.判断订单状态
        OrderMaster orderMaster = orderMasterOptional.get();
        if (OrderStatusEnum.NEW.getCode() != orderMaster.getOrderStatus()) {
            throw new OrderException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //查询订单详情
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new OrderException(ResultEnum.ORDER_DETAIL_NOT_EXIST);
        }

        //3.修改订单状态为完结
        orderMaster.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        orderMasterRepository.save(orderMaster);

        OrderDTO orderDTO = new OrderDTO();
        BeanUtils.copyProperties(orderMaster,orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    //如果是秒杀类项目，为了让系统抗住很大的并发压力，可以把3，4两步改为异步处理。
    //123成功之后就告诉用户下单成功，4异步处理{下单}（消息队列），如果第四步失败了，则进行重试，不处理成功，mq的消息一直在
    //若34都异步处理呢，如果订单服务创建订单成功，商品服务扣减库存失败，该如何回滚这笔一单子呢？
    //思路：订单服务创建成功后，订单状态变为下单中，发送一个消息给队列，产品服务收到队列里面的消息，进行减库存，如果成功，
    //则给队列发送成功的消息，订单服务监听扣库存的结果，此时订单服务把订单状态改为下单成功；若给队列发送失败的消息，则把订单状态改为下单失败。给用户
    //刚开始返回的是下单中，等成功或者失败了就返回用户成功或者失败。类似于12306抢票
    //但是这个的前提是投递消息是可靠的。

    /*如果秒杀类项目，查询商品信息也应该做相应处理
    *  1.将商品的信息，比如商品价格，商品id，商品库存等存储在redis中
    *  2.判断库存是不是足够，如果够，则减库存并将新值设置进redis中。
    *  3.后续进行订单服务订单入库，如果订单详细入库成功，订单主表入库失败，则进行事物回滚，都不进行入库，那redis库存已经
    *  变化了，该如何处理呢？
    *  4.订单入库异常的情况下，手动回滚redis，订单入库的地方，如果入库失败，则把库存又加回redis。
    * */
}
