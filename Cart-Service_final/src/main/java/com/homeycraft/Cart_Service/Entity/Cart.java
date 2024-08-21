package com.homeycraft.Cart_Service.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "Cart")
public class Cart
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @Column(unique = true)
    private String emailId;
    private List<Long> productList;
    private BigDecimal totalPrice ;

}
