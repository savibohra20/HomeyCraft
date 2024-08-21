package com.homeycraft.product_catalog_service.repository;

import com.homeycraft.product_catalog_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product , Long>
{

    public List<Product> findByName(String name);

    public List<Product> findByCategory(String category);

    public List<Product> findByBrand(String brand);
    public List<Product> findBySellerEmailId(String sellerEmailId);
    Optional<Product> findByNameAndSellerEmailId(String name , String sellerEmailId);
}
