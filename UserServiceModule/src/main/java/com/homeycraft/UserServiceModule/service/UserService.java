package com.homeycraft.UserServiceModule.service;

import java.util.List;

import com.homeycraft.UserServiceModule.exceptions.EmailIdAlreadyExistException;
import com.homeycraft.UserServiceModule.exceptions.EmailIdNotExistException;
import com.homeycraft.UserServiceModule.model.User;

public interface UserService {
    User registerUser(User user) throws EmailIdAlreadyExistException;
    User getUserByEmailId(String emailId) throws EmailIdNotExistException;
    User updateUser(User user) throws EmailIdNotExistException;
    boolean deleteUser(String emailId);
    List<User> getAllUser();
    boolean validateUser(User user);
    List<User> getUsersByRole(String role); // New method to get users by role
}
