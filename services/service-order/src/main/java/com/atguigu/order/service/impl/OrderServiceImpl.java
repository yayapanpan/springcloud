package com.atguigu.order.service.impl;



import com.atguigu.order.bean.Order;
import com.atguigu.order.service.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.function.ObjLongConsumer;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public Order createOrder(Long productId, Long userId) {
        Order order = new Order();
        order.setId(1L);
        order.setTotalAmount(new BigDecimal("0"));
        order.setUserId(userId);
        order.setNikeName("zhangsan");
        order.setAddress("shangguigu");
        //TODO 远程查询商品列表
        order.setProductList(null);


        return order;
    }
}
