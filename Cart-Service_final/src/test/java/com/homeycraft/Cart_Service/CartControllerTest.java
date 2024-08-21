package com.homeycraft.Cart_Service;

import com.homeycraft.Cart_Service.DTO.Product;
import com.homeycraft.Cart_Service.Entity.Cart;
import com.homeycraft.Cart_Service.Service.CartService;
import com.homeycraft.Cart_Service.Controller.CartController;
import com.homeycraft.Cart_Service.Service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
        import static org.mockito.Mockito.*;

class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private JwtService jwtService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testAddCart_Unauthorized() {
        String token = "mocked-token";
        String emailId = "user@example.com";
        Cart cart = new Cart();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.validateToken(token, emailId, "Buyer")).thenReturn(false);

        ResponseEntity<?> response = cartController.addCart(emailId, cart, request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Unauthorized", response.getBody());
    }


    @Test
    void testGetAllCart() {
        Cart cart1 = new Cart();
        Cart cart2 = new Cart();
        List<Cart> cartList = Arrays.asList(cart1, cart2);

        when(cartService.getAllCart()).thenReturn(cartList);

        ResponseEntity<?> response = cartController.getAllCart();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartList, response.getBody());
    }

    @Test
    void testGetCartById_CartFound() {
        Cart cart = new Cart();

        when(cartService.getCartById(1L)).thenReturn(cart);

        ResponseEntity<?> response = cartController.getCartById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    void testGetCartById_CartNotFound() {
        when(cartService.getCartById(1L)).thenReturn(null);

        ResponseEntity<?> response = cartController.getCartById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cart Id Not Found", response.getBody());
    }

    @Test
    void testGetCartByEmailID_CartFound() {
        Cart cart = new Cart();
        String emailId = "user@example.com";

        when(cartService.getCartByEmailID(emailId)).thenReturn(Optional.of(cart));

        ResponseEntity<?> response = cartController.getCartByEmailID(emailId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cart, response.getBody());
    }

    @Test
    void testGetCartByEmailID_CartNotFound() {
        String emailId = "user@example.com";

        when(cartService.getCartByEmailID(emailId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = cartController.getCartByEmailID(emailId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cart not found for email ID: " + emailId, response.getBody());
    }

    @Test
    void testDeleteCart_CartFound() {
        when(cartService.deleteCart(1L)).thenReturn(true);

        ResponseEntity<?> response = cartController.deleteCart(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cart is deleted successfully", response.getBody());
    }

    @Test
    void testDeleteCart_CartNotFound() {
        when(cartService.deleteCart(1L)).thenReturn(false);

        ResponseEntity<?> response = cartController.deleteCart(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Cart Id Not Found", response.getBody());
    }

    @Test
    void testPlaceOrder_Success() {
        Cart cart = new Cart();
        cart.setTotalPrice(BigDecimal.valueOf(100));

        when(cartService.getCartById(1L)).thenReturn(cart);

        ResponseEntity<?> response = cartController.placeOrder(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Order Placed \nTotal amount to be paid : " + cart.getTotalPrice(), response.getBody());
    }

    @Test
    void testPlaceOrder_CartEmpty() {
        Cart cart = new Cart();
        cart.setTotalPrice(BigDecimal.ZERO);

        when(cartService.getCartById(1L)).thenReturn(cart);

        ResponseEntity<?> response = cartController.placeOrder(1L);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cart is empty. Cannot place an order.", response.getBody());
    }
}