package com.atguigu.order.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Data
public class Order {
    private Long id;
    private BigDecimal totalAmount;
    private Long userId;
    private String nikeName;
    private String address;
    private List<Objects> productList;
}
