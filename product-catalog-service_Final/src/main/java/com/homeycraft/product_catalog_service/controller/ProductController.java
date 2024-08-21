package com.homeycraft.product_catalog_service.controller;

import com.homeycraft.product_catalog_service.entity.Product;
import com.homeycraft.product_catalog_service.entity.UserContext;
import com.homeycraft.product_catalog_service.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products/product")
public class ProductController
{
    @Autowired
    private ProductService productService;

    private UserContext getUserContext(HttpServletRequest request) {
        return (UserContext) request.getAttribute("userContext");
    }

    private ResponseEntity<String> checkAuthorization(UserContext userContext, String expectedEmailId, String expectedRole)
    {
        String emailId = userContext.getEmailId();
        String role = userContext.getRole();
        if (!emailId.equalsIgnoreCase(expectedEmailId) || !role.equalsIgnoreCase(expectedRole)) {
            return new ResponseEntity<>("Not Authorized", HttpStatus.UNAUTHORIZED);
        }
        return null;
    }


    @GetMapping("/allProducts/{emailId}")
    public ResponseEntity<?> getAllProduct(@PathVariable("emailId") String userEmailID , HttpServletRequest request)
    {
        UserContext userContext = getUserContext(request);
        ResponseEntity<String> authorizationResponse = checkAuthorization(userContext, userEmailID, "Buyer");
        if (authorizationResponse != null)
        {
            return authorizationResponse;
        }

        List<Product> productList = productService.getAllProduct();
        return new ResponseEntity<>(productList , HttpStatus.OK);
    }

    @GetMapping("/{emailId}/id/{id}")
    public ResponseEntity<?> getAllProductById(@PathVariable("emailId") String userEmailId ,@PathVariable("id") Long id , HttpServletRequest request)
    {
        UserContext userContext = getUserContext(request);
        ResponseEntity<String> authorizationResponse = checkAuthorization(userContext, userEmailId, "Buyer");
        if (authorizationResponse != null)
        {
            return authorizationResponse;
        }

        Product product = productService.getAllProductById(id);
        return product != null ?
                new ResponseEntity<>(product , HttpStatus.OK)
                : new ResponseEntity<>(product , HttpStatus.NOT_FOUND);
    }


    @GetMapping("/{emailId}/name/{name}")
    public ResponseEntity<?> getAllProductByName(@PathVariable("emailId") String userEmailId , @PathVariable("name") String name, HttpServletRequest request )
    {
        UserContext userContext = getUserContext(request);
        ResponseEntity<String> authorizationResponse = checkAuthorization(userContext, userEmailId, "Buyer");
        if (authorizationResponse != null)
        {
            return authorizationResponse;
        }

        List<Product> products = productService.getAllProductByName(name);
        return products != null
                ? new ResponseEntity<>(products , HttpStatus.OK)
                : new ResponseEntity<>(products , HttpStatus.NOT_FOUND) ;

    }

    @GetMapping("/{emailId}/category/{category}")
    public ResponseEntity<?> getAllProductByCategory(@PathVariable("emailId") String userEmailId , @PathVariable("category") String category, HttpServletRequest request )
    {
        UserContext userContext = getUserContext(request);
        ResponseEntity<String> authorizationResponse = checkAuthorization(userContext, userEmailId, "Buyer");
        if (authorizationResponse != null)
        {
            return authorizationResponse;
        }

        List<Product> products = productService.getAllProductByCategory(category);
        return products != null
                ? new ResponseEntity<List<Product>>(products , HttpStatus.OK)
                : new ResponseEntity<List<Product>>(products , HttpStatus.NOT_FOUND);

    }

    @GetMapping("/{emailId}/brand/{brand}")
    public ResponseEntity<?> getAllProductByBrand(@PathVariable("emailId") String userEmailId ,@PathVariable("brand") String brand, HttpServletRequest request )
    {

        UserContext userContext = getUserContext(request);
        ResponseEntity<String> authorizationResponse = checkAuthorization(userContext, userEmailId, "Buyer");
        if (authorizationResponse != null)
        {
            return authorizationResponse;
        }

        List<Product> products = productService.getAllProductByBrand(brand);
        return products != null
                ? new ResponseEntity<List<Product>>(products , HttpStatus.OK)
                : new ResponseEntity<List<Product>>(products , HttpStatus.NOT_FOUND);
    }

}
