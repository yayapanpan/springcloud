package com.atguigu.order.config;

import feign.Logger;
import feign.Retryer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;



@Configuration
public class OrderConfig {
//重试器
//    @Bean
//    Retryer retryer(){
//        return new Retryer.Default();
//    }

    @Bean
    Logger.Level feignLogLevel(){
        return Logger.Level.FULL;
    }


    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
