package com.homeycraft.Cart_Service.Service;

import com.homeycraft.Cart_Service.DTO.Order;
import com.homeycraft.Cart_Service.Entity.Cart;

import java.util.List;
import java.util.Optional;

public interface CartService
{
    public Cart addCart(Cart cart);
    public List<Cart> getAllCart();
    public Cart  getCartById(Long cartId);
    public Optional<Cart> getCartByEmailID(String emailId);
    public  boolean deleteCart(Long cartId);
//    Order placeOrder(String emailId);

}







