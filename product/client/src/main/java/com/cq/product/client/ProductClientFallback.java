package com.cq.product.client;

import com.cq.model.DecreaseStockInput;
import com.cq.model.ProductInfoOutput;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Auther: caoqsq
 * @Date: 2018/7/2 15:21
 * @Description: fegin中引入hystrix,服务降级类
 */
@Component
public class ProductClientFallback implements ProductClient {
    @Override
    public List<ProductInfoOutput> listForOrder(List<String> productIdList) {
        return null;//如果订单服务中返回null说明服务降级了
    }

    @Override
    public void decreaseStock(List<DecreaseStockInput> decreaseStockInputList) {

    }
}
