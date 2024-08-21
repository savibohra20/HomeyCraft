package com.homeycraft.Cart_Service.Service;

import com.homeycraft.Cart_Service.DTO.Order;
import com.homeycraft.Cart_Service.Entity.Cart;
import com.homeycraft.Cart_Service.Exception.CartNotFoundException;
import com.homeycraft.Cart_Service.Repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImplemenation  implements CartService
{
    @Autowired
    private CartRepository cartRepository;

    @Override
    public Cart addCart(Cart cart) {
        return cartRepository.save(cart);
    }

    @Override
    public List<Cart> getAllCart() {
        return cartRepository.findAll();
    }

    @Override
    public Cart getCartById(Long cartId)
    {
        Optional<Cart> optionalCart = cartRepository.findById(cartId);
        if (optionalCart.isPresent()) {
            return optionalCart.get();
        } else {
            // Handle the case where the cart is not found, for example, by throwing an exception
            throw new CartNotFoundException("Cart not found with ID: " + cartId);
        }
    }

    @Override
    public Optional<Cart> getCartByEmailID(String emailId) {
        // Assuming you have a method in the repository to find by email ID
        return cartRepository.findByEmailId(emailId);
    }

    @Override
    public boolean deleteCart(Long cartId) {
        Optional<Cart> optional =cartRepository.findById(cartId);
        boolean isDeleted=false;
        if (optional.isPresent()) {
            cartRepository.deleteById(cartId);
            isDeleted= true;
        }
        return isDeleted;
    }

}
