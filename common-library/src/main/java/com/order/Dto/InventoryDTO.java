package com.order.Dto;

import lombok.Data;

@Data
public class InventoryDTO {
    private Long id;
    private String productName;
    private int stock;
}
