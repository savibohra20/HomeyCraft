package com.homeycraft.Cart_Service.Controller;

import com.homeycraft.Cart_Service.DTO.Order;
import com.homeycraft.Cart_Service.DTO.Product;
import com.homeycraft.Cart_Service.Entity.Cart;
import com.homeycraft.Cart_Service.Entity.UserContext;
import com.homeycraft.Cart_Service.Service.CartService;
import com.homeycraft.Cart_Service.Service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/carts")
public class CartController
{
    @Autowired
    private CartService cartService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RestTemplate restTemplate;

    private UserContext getUserContext(HttpServletRequest request)
    {
        return (UserContext) request.getAttribute("userContext");
    }


    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }


    @PostMapping("/cart/{emailId}")
    public ResponseEntity<?> addCart(@PathVariable("emailId") String userEmailID, @RequestBody Cart cart, HttpServletRequest request) {
        // Extract the JWT token from the Authorization header
        String token = extractTokenFromRequest(request);

        // Validate token for the specified email ID and role 'Buyer'
        if (!jwtService.validateToken(token, userEmailID, "Buyer")) {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }

        BigDecimal totalPrice = BigDecimal.ZERO;

        // Calculate total price for the cart
        for (Long productId : cart.getProductList()) {
            try {
                Product product = fetchProductDetails(productId, token , userEmailID);
                if (product != null) {
                    totalPrice = totalPrice.add(product.getPrice());
                }
            } catch (HttpClientErrorException e) {
                // Handle HTTP errors (e.g., 4xx and 5xx status codes)
                return new ResponseEntity<>("Error fetching product details: " + e.getMessage(), HttpStatus.CONFLICT);
            } catch (Exception e) {
                // Handle other errors
                return new ResponseEntity<>("An error occurred while processing products: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Set the total price and save the cart
        cart.setTotalPrice(totalPrice);
        Cart savedCart = cartService.addCart(cart);
        return new ResponseEntity<>(savedCart, HttpStatus.CREATED);
    }


    private Product fetchProductDetails(Long productId, String token , @PathVariable("emailId") String userEmailID)
    {
        String productServiceUrl = "http://localhost:8080/products/product/" + userEmailID + "/id/" + productId;

        // Create headers to include the JWT token
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        // Create an HttpEntity object with headers
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // Make a call to the Product Service to get product details
            ResponseEntity<Product> response = restTemplate.exchange(productServiceUrl, HttpMethod.GET, entity, Product.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            // Handle HTTP errors (e.g., 4xx and 5xx status codes)
            throw new RuntimeException("Error fetching product details: " + e.getMessage());
        } catch (Exception e) {
            // Handle other errors
            throw new RuntimeException("An error occurred while fetching product details: " + e.getMessage());
        }
    }

    @GetMapping("/allCart")
    public ResponseEntity<?> getAllCart()
    {
        List<Cart> cartList = cartService.getAllCart();
        return new ResponseEntity<>(cartList, HttpStatus.OK);
    }

    @GetMapping("/cart/{id}")
    public ResponseEntity<?> getCartById(@PathVariable("id") Long cartId)
    {
        Cart cart = cartService.getCartById(cartId);
        ResponseEntity entity = new ResponseEntity<>("Cart Id Not Found", HttpStatus.NOT_FOUND);
        if (cart != null) {
            entity = new ResponseEntity<>(cart, HttpStatus.OK);
        }
        return entity;
    }

    @GetMapping("/{emailId}")
    public ResponseEntity<?> getCartByEmailID(@PathVariable("emailId") String emailId) {
        Optional<Cart> optionalCart = cartService.getCartByEmailID(emailId);
        if (optionalCart.isPresent()) {
            return ResponseEntity.ok(optionalCart.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found for email ID: " + emailId);
        }
    }

    @DeleteMapping("/deleteCart/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable("id") Long id)
    {
        boolean isDeleted = cartService.deleteCart(id);
        ResponseEntity<?> entity = new ResponseEntity<>("Cart Id Not Found", HttpStatus.NOT_FOUND);
        if (isDeleted) {
            entity = new ResponseEntity<>("Cart is deleted successfully", HttpStatus.OK);
        }
        return entity;
    }

    @PostMapping("/order/{id}")
    public ResponseEntity<?> placeOrder(@PathVariable("id") Long cartId)
    {
        Cart cart = cartService.getCartById(cartId);
        if (cart.getTotalPrice() == null || cart.getTotalPrice().compareTo(BigDecimal.ZERO) == 0)
        {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Cart is empty. Cannot place an order.");
        }
        return ResponseEntity.status(200).body("Order Placed \nTotal amount to be paid : " + cart.getTotalPrice() );

    }


}

