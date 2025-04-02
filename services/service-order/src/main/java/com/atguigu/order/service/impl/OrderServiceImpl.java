package com.atguigu.order.service.impl;



import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.atguigu.order.bean.Order;
import com.atguigu.order.feign.ProductFeignClient;
import com.atguigu.order.service.OrderService;
import com.atguigu.product.bean.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.ObjLongConsumer;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    DiscoveryClient discoveryClient;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LoadBalancerClient loadBalancerClient;
    @Autowired
    ProductFeignClient productFeignClient;
    @SentinelResource(value = "createOrder",blockHandler = "createOrderFallback")
    @Override
    public Order createOrder(Long productId, Long userId) {
//        Product product = getProductFromRemote(productId);
//        Product product = getProductFromRemoteWithLoadBalance(productId);
//        Product product = getProductFromRemoteWithLoadBalanceAnnotation(productId);
        //使用feign完成远程调用
        Product product = productFeignClient.getProductById(productId);
        Order order = new Order();
        order.setId(1L);
        //TODO总金额
        order.setTotalAmount(product.getPrice().multiply(new BigDecimal(product.getNum())));
        order.setUserId(userId);
        order.setNikeName("zhangsan");
        order.setAddress("shangguigu");
        //TODO 查询商品列表
        order.setProductList(Arrays.asList(product));


        return order;
    }
    //兜底回调
    public Order createOrderFallback(Long productId, Long userId, BlockException e) {
        Order order = new Order();
        order.setId(0l);
        order.setTotalAmount(new BigDecimal("0"));
        order.setUserId(userId);
        order.setNikeName("未知用户");
        order.setAddress("异常信息"+e.getClass());

        return order;
    }

    private Product getProductFromRemote(Long productId){
        List<ServiceInstance> instances = discoveryClient.getInstances("service-product");

        ServiceInstance instance = instances.get(0);
        //http://localhost:9002/product/4
        //远程url地址
        String url = "http://"+instance.getHost()+":"+instance.getPort()+"/product/"+productId;
        log.info("url:"+url);
        //给远程发送请求
        Product product = restTemplate.getForObject(url, Product.class);
        return product;
    }
    //完成负载均衡发送请求
    private Product getProductFromRemoteWithLoadBalance(Long productId){

        ServiceInstance choose = loadBalancerClient.choose("service-product");
        String url = "http://"+choose.getHost()+":"+choose.getPort()+"/product/"+productId;
        //http://localhost:9002/product/4
        //远程url地址
//        String url = "http://"+instance.getHost()+":"+instance.getPort()+"/product/"+productId;
        log.info("url:"+url);
        //给远程发送请求
        Product product = restTemplate.getForObject(url, Product.class);
        return product;
    }

    //注解式负载均衡
    //完成负载均衡发送请求

    private Product getProductFromRemoteWithLoadBalanceAnnotation(Long productId){

//        ServiceInstance choose = loadBalancerClient.choose("service-product");
        String url = "http://service-product/product/"+productId;
        //service-product会动态替换
        //http://localhost:9002/product/4
        //远程url地址
        //给远程发送请求
        Product product = restTemplate.getForObject(url, Product.class);
        return product;
    }
}
