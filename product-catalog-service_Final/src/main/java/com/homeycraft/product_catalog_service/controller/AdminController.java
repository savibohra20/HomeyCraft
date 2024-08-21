package com.homeycraft.product_catalog_service.controller;

import com.homeycraft.product_catalog_service.entity.Product;
import com.homeycraft.product_catalog_service.entity.UserContext;
import com.homeycraft.product_catalog_service.services.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products/admin")
public class AdminController
{
    @Autowired
    private ProductService productService;

    private UserContext getUserContext(HttpServletRequest request)
    {
        return (UserContext) request.getAttribute("userContext");
    }

    private ResponseEntity<String> checkAuthorization(UserContext userContext, String expectedEmailId, String expectedRole) {
        String emailId = userContext.getEmailId();
        String role = userContext.getRole();
        if (!emailId.equalsIgnoreCase(expectedEmailId) || !role.equalsIgnoreCase(expectedRole.toLowerCase()))
        {
            System.out.println("i am heree");
            return new ResponseEntity<>("Not Authorized", HttpStatus.UNAUTHORIZED);
        }
        return null;
    }

    @GetMapping("/{emailId}/products")
    public ResponseEntity<?> getAllProductByEmailID(@PathVariable("emailId") String userEmailID , HttpServletRequest request)
    {
        UserContext userContext = getUserContext(request);
        ResponseEntity<String> authorizationResponse = checkAuthorization(userContext, userEmailID, "seller");
        if (authorizationResponse != null)
        {
            return authorizationResponse;
        }

        List<Product> productList = productService.getAllProductBySellerEmailID(userEmailID);
        List<Product> uniqueProductList = productList.stream()
                                          .distinct()
                                          .collect(Collectors.toList());
        return new ResponseEntity<>(uniqueProductList , HttpStatus.OK);
    }

    @PostMapping("/{emailId}/addProduct")
    public ResponseEntity<?> addProduct(@RequestBody Product product , @PathVariable("emailId") String userEmailID  ,HttpServletRequest request)
    {
        UserContext userContext = getUserContext(request);
        ResponseEntity<String> authorizationResponse = checkAuthorization(userContext, userEmailID, "seller");
        if (authorizationResponse != null)
        {
            System.out.println("i am in add produt ");
            return authorizationResponse;
        }

        try {
            product.setSellerEmailId(userEmailID);
            productService.addProduct(product);
            return new ResponseEntity<Product>(product , HttpStatus.CREATED);
        }catch (Exception e)
        {
            return new ResponseEntity<>( "An error occurred: " + e.getMessage() ,HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @DeleteMapping("/{emailId}/deleteProducts/{id}")
    private ResponseEntity<?> deleteProduct(  @PathVariable("emailId") String userEmailID ,@PathVariable("id") Long id ,HttpServletRequest request)
    {
        UserContext userContext = getUserContext(request);
        ResponseEntity<String> authorizationResponse = checkAuthorization(userContext, userEmailID, "seller");
        if (authorizationResponse != null)
        {
            return authorizationResponse;
        }

        boolean isDeleted = productService.deleteProduct(id);
        return isDeleted ? new ResponseEntity<>("Deleted Product Successfully" ,HttpStatus.OK)
                : new ResponseEntity<>("Product Not Exist" ,HttpStatus.GONE) ;
    }


    @PutMapping("/{emailId}/updateProducts/{id}")
    private ResponseEntity<?> updateProduct( @RequestBody Product product , @PathVariable("emailId") String userEmailID ,@PathVariable("id") Long id ,HttpServletRequest request)
    {
        UserContext userContext = getUserContext(request);
        ResponseEntity<String> authorizationResponse = checkAuthorization(userContext, userEmailID, "seller");
        if (authorizationResponse != null)
        {
            return authorizationResponse;
        }

        product.setId(id);
        Product updatedProduct = productService.updateProduct(id , product);

        return (updatedProduct !=  null ) ? new ResponseEntity<>(product , HttpStatus.OK)
        : new ResponseEntity<>( product ,HttpStatus.BAD_REQUEST);
    }

}
