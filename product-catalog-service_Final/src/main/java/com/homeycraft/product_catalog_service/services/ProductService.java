package com.homeycraft.product_catalog_service.services;

import com.homeycraft.product_catalog_service.entity.Product;
import com.homeycraft.product_catalog_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;


import java.util.List;

@Service
public class ProductService
{
    @Autowired
    private ProductRepository productRepository;

    private Product product;
    public List<Product> getAllProduct()
    {
        return productRepository.findAll();
    }

    public void addProduct(Product product)
    {
//        Optional<Product> existingProduct = productRepository.findByNameAndSellerEmailId(product.getName() , product.getSellerEmailId());
//        System.out.println("here");
//        if (!existingProduct.isPresent()) {
//            productRepository.save(product);
//            System.out.println("Product added successfully.");
//        } else {
//            throw new IllegalArgumentException("A product with the same name and seller email ID already exists.");
//        }
        productRepository.save(product);
    }

    public Product getAllProductById(Long id)
    {
        return productRepository.findById(id).orElse(null);
    }

    public boolean deleteProduct(Long id)
    {
        if (productRepository.existsById(id))
        {
        productRepository.deleteById(id);
        return true;
        }
        return false;
    }

    public Product updateProduct(Long id, Product product)
    {
        if (product == null)
        {
            throw new IllegalArgumentException("Product cannot be null");
        }
        Optional<Product> existingProductOpt = productRepository.findById(id);

        if (existingProductOpt.isPresent())
        {
            Product existingProduct = existingProductOpt.get();

            // Update the existing product with new values
            existingProduct.setName(product.getName());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setBrand(product.getBrand());
            existingProduct.setQuantity(product.getQuantity());
            existingProduct.setAvailability(product.isAvailability());

            // Save the updated product back to the repository
            return productRepository.save(existingProduct);
        }
        else
        {
            // Handle the case where the product does not exist
            throw new RuntimeException("Product with ID " + id + " not found");
        }
    }

    public List<Product> getAllProductByName(String name)
    {
        return productRepository.findByName(name);
    }

    public List<Product> getAllProductByCategory(String category)
    {
        return productRepository.findByCategory(category);
    }

    public List<Product> getAllProductByBrand(String brand)
    {
        return productRepository.findByBrand(brand);

    }

    public List<Product> getAllProductBySellerEmailID(String sellerEmailId)
    {
        return productRepository.findBySellerEmailId(sellerEmailId);

    }



}



