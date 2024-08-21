package com.homeycraft.Cart_Service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
public class Order{
    private Long id;
    private String emailId;
    private List<Long> products;
    private BigDecimal totalPrice;
    private String status; // "pending", "shipped", "delivered"
}