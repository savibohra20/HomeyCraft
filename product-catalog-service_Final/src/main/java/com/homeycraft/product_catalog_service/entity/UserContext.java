package com.homeycraft.product_catalog_service.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UserContext
{
    private String emailId;
    private String role ;

}
