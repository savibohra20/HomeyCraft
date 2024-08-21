package com.homeycraft.UserServiceModule.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.homeycraft.UserServiceModule.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmailIdAndPassword(String emailId, String password);

    List<User> findByRole(String role); // New method to find users by role
}