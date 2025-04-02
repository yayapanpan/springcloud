package com.atguigu.order.feign;


import com.atguigu.order.feign.fallback.ProductFeignClientFallback;
import com.atguigu.product.bean.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-product",fallback = ProductFeignClientFallback.class)//feign客户端
public interface ProductFeignClient {


    //mvc注解的两套使用逻辑
    //1.标注在controller上，是接受请求
    //2.标注在FeignClient上，是接收这样的请求
    @GetMapping("/product/{id}")
    Product getProductById(@PathVariable("id") Long id);
}
