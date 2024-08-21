package com.homeycraft.product_catalog_service.controller;

import com.homeycraft.product_catalog_service.entity.Product;
import com.homeycraft.product_catalog_service.entity.UserContext;
import com.homeycraft.product_catalog_service.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringJUnitConfig
public class ProductControllerTests {

    @InjectMocks
    private ProductController productController;

    @Mock
    private ProductService productService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private UserContext createUserContext(String emailId, String role) {
        UserContext userContext = new UserContext();
        userContext.setEmailId(emailId);
        userContext.setRole(role);
        return userContext;
    }

    @Test
    void testGetAllProduct() {
        String userEmailID = "buyer@example.com";
        UserContext userContext = createUserContext(userEmailID, "Buyer");
        when(request.getAttribute("userContext")).thenReturn(userContext);

        List<Product> products = new ArrayList<>();
        products.add(new Product());

        when(productService.getAllProduct()).thenReturn(products);

        ResponseEntity<?> response = productController.getAllProduct(userEmailID, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }

    @Test
    void testGetAllProductByIdFound() {
        String userEmailID = "buyer@example.com";
        Long id = 1L;
        Product product = new Product();
        UserContext userContext = createUserContext(userEmailID, "Buyer");
        when(request.getAttribute("userContext")).thenReturn(userContext);

        when(productService.getAllProductById(id)).thenReturn(product);

        ResponseEntity<?> response = productController.getAllProductById(userEmailID, id, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(product, response.getBody());
    }

    @Test
    void testGetAllProductByIdNotFound() {
        String userEmailID = "buyer@example.com";
        Long id = 1L;
        UserContext userContext = createUserContext(userEmailID, "Buyer");
        when(request.getAttribute("userContext")).thenReturn(userContext);

        when(productService.getAllProductById(id)).thenReturn(null);

        ResponseEntity<?> response = productController.getAllProductById(userEmailID, id, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(null, response.getBody());
    }

    @Test
    void testGetAllProductByNameFound() {
        String userEmailID = "buyer@example.com";
        String name = "TestProduct";
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        UserContext userContext = createUserContext(userEmailID, "Buyer");
        when(request.getAttribute("userContext")).thenReturn(userContext);

        when(productService.getAllProductByName(name)).thenReturn(products);

        ResponseEntity<?> response = productController.getAllProductByName(userEmailID, name, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }

//    @Test
//    void testGetAllProductByNameNotFound() {
//        String userEmailID = "buyer@example.com";
//        String name = "NonExistentProduct";
//        List<Product> products = new ArrayList<>();
//        UserContext userContext = createUserContext(userEmailID, "Buyer");
//        when(request.getAttribute("userContext")).thenReturn(userContext);
//
//        when(productService.getAllProductByName(name)).thenReturn(products);
//
//        ResponseEntity<?> response = productController.getAllProductByName(userEmailID, name, request);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals(products, response.getBody());
//    }

    @Test
    void testGetAllProductByCategoryFound() {
        String userEmailID = "buyer@example.com";
        String category = "Electronics";
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        UserContext userContext = createUserContext(userEmailID, "Buyer");
        when(request.getAttribute("userContext")).thenReturn(userContext);

        when(productService.getAllProductByCategory(category)).thenReturn(products);

        ResponseEntity<?> response = productController.getAllProductByCategory(userEmailID, category, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }

//    @Test
//    void testGetAllProductByCategoryNotFound() {
//        String userEmailID = "buyer@example.com";
//        String category = "NonExistentCategory";
//        List<Product> products = new ArrayList<>();
//        UserContext userContext = createUserContext(userEmailID, "Buyer");
//        when(request.getAttribute("userContext")).thenReturn(userContext);
//
//        when(productService.getAllProductByCategory(category)).thenReturn(products);
//
//        ResponseEntity<?> response = productController.getAllProductByCategory(userEmailID, category, request);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals(products, response.getBody());
//    }

    @Test
    void testGetAllProductByBrandFound() {
        String userEmailID = "buyer@example.com";
        String brand = "BrandName";
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        UserContext userContext = createUserContext(userEmailID, "Buyer");
        when(request.getAttribute("userContext")).thenReturn(userContext);

        when(productService.getAllProductByBrand(brand)).thenReturn(products);

        ResponseEntity<?> response = productController.getAllProductByBrand(userEmailID, brand, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(products, response.getBody());
    }
//
//    @Test
//    void testGetAllProductByBrandNotFound() {
//        String userEmailID = "buyer@example.com";
//        String brand = "NonExistentBrand";
//        List<Product> products = new ArrayList<>();
//        UserContext userContext = createUserContext(userEmailID, "Buyer");
//        when(request.getAttribute("userContext")).thenReturn(userContext);
//
//        when(productService.getAllProductByBrand(brand)).thenReturn(products);
//
//        ResponseEntity<?> response = productController.getAllProductByBrand(userEmailID, brand, request);
//
//        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
//        assertEquals(products, response.getBody());
//    }
}
