package com.homeycraft.UserServiceModule.model;


import java.util.List;

import org.springframework.data.annotation.Id;
import lombok.Data;

@Data
public class Cart {
    @Id
    private int cartId;
    private String emailId;
    private List<Item> itemList;
}
