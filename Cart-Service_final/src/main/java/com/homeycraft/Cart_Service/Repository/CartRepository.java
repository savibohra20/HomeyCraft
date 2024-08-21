package com.homeycraft.Cart_Service.Repository;

import com.homeycraft.Cart_Service.Entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart , Long>
{
    public Optional<Cart> findByEmailId(String emailId);




}
