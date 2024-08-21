package com.homeycraft.UserServiceModule.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.homeycraft.UserServiceModule.exceptions.EmailIdAlreadyExistException;
import com.homeycraft.UserServiceModule.exceptions.EmailIdNotExistException;
import com.homeycraft.UserServiceModule.model.User;
import com.homeycraft.UserServiceModule.service.UserService;

import io.jsonwebtoken.Jwts;

@RestController
@RequestMapping("/api/v1/users")
public class UserServiceController {
    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate  restTemplate;

    @PostMapping("/user")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        ResponseEntity<?> entity;
        try {
            User user1 = userService.registerUser(user);
            entity = new ResponseEntity<>("User Registered Successfully...", HttpStatus.CREATED);
        } catch (EmailIdAlreadyExistException e) {
            entity = new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
        return entity;
    }

    @PostMapping("/login")
    public ResponseEntity<?> validateUser(@RequestBody User user) {
        ResponseEntity<?> entity=null;
        if (userService.validateUser(user)) {
            //	entity = new ResponseEntity<>("User Logged in Successfully...", HttpStatus.OK);
            String token =getToken(user.getEmailId());
            entity = new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            entity = new ResponseEntity<>("Invalid User Credentials...", HttpStatus.UNAUTHORIZED);
        }
        return entity;
    }


    private String getToken(String emailId) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setSubject(emailId)
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, "FYNDNA-SUCCESS")
                .compact();
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) throws EmailIdNotExistException {
        User user = userService.getUserByEmailId(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody User user) throws EmailIdNotExistException {
        user.setEmailId(id);
        User updatedUser = userService.updateUser(user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) throws EmailIdNotExistException {
        boolean isDeleted =userService.deleteUser(id);
        ResponseEntity<?> entity= new ResponseEntity<>("Email Id Not Exist", HttpStatus.NOT_FOUND);
        if (isDeleted) {
            entity =new ResponseEntity<>("User with id: " + id + " deleted successfully", HttpStatus.OK);
        }
        return entity;
    }

    @GetMapping("/role/{role}")
    public ResponseEntity<?> getUsersByRole(@PathVariable String role) {
        return new ResponseEntity<>(userService.getUsersByRole(role), HttpStatus.OK);
    }

}