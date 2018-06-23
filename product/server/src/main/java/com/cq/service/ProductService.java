package com.cq.service;

import com.cq.model.DecreaseStockInput;
import com.cq.model.ProductInfo;
import com.cq.model.ProductInfoOutput;

import java.util.List;

/**
 * @Auther: caoqsq
 * @Date: 2018/6/20 16:24
 * @Description:
 */
public interface ProductService {

    /**
     * 查询所有在架商品列表
     */
    List<ProductInfo> findUpAll();

    /**
     * 查询商品列表
     */
    List<ProductInfoOutput> findList(List<String> productIdList);

    /**
     * 扣库存
     */
    void decreaseStock(List<DecreaseStockInput> decreaseStockInputList);
}
