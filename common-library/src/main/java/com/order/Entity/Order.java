package com.order.Entity;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "order_table")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private int quantity;
    private double price;
}
