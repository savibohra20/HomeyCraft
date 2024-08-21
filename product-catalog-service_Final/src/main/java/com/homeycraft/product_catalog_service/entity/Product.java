package com.homeycraft.product_catalog_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.NonFinal;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Products_Table")
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_ID")
    private Long id;

    @Column(name = "Product_Name")
    @NotNull
    private String name;

    @Column(name = "Price")
    @NotNull
    private BigDecimal price ;

    @Column(name = "Description")
    @NotNull
    private String description;

    @Column(name = "Category")
    @NotNull
    private String category;

    @Column(name = "Brand")
    @NonNull
    private String brand;

    @Column(name = "Quantity")
    @NotNull
    private int quantity;

    @Column(name = "Available")
    private boolean availability;

    @Column(name = "Seller_Email")
    private String sellerEmailId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
