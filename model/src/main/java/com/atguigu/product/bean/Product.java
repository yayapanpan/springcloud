package com.atguigu.product.bean;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class Product {
    private long id;
    private BigDecimal price;
    private String productName;
    private int num;
}
