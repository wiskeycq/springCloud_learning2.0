package com.cq.message.product;

import com.cq.model.ProductInfoOutput;
import com.cq.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/27 11:15
 * @Description: 监控产品服务，当执行完全部减库存后，把消息发到队列，再把库存信息存进redis
 */
@Component
@Slf4j
public class ProductInfoReceiver {

    private static final String PRODUCT_STOCK_TEMPLATE="product_stock_%s";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RabbitListener(queuesToDeclare = @Queue("productInfo"))
    public void process(String message) {
        List<ProductInfoOutput> outputList = (List<ProductInfoOutput>)JsonUtil.fromJson(message,
                new TypeReference<List<ProductInfoOutput>>() {});
        log.info("从队列{}查询出来的消息为:{}","productInfo",outputList);

        //接收的消息存储在redis里
        for (ProductInfoOutput output : outputList) {
            stringRedisTemplate.opsForValue().set(String.format(PRODUCT_STOCK_TEMPLATE,output.getProductId()),
                    String.valueOf(output.getProductStock()));
        }
    }
}
