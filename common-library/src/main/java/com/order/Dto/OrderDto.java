package com.order.Dto;

import lombok.Data;

@Data
public class OrderDto {
    private Long id;
    private String productName;
    private int quantity;
    private double price;
}
