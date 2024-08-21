package com.homeycraft.UserServiceModule.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @Column(length = 30)
    private String emailId;

    @Column(length = 30)
    private String password;

    @Column(length = 30)
    private String username;

    @Column(length = 15)
    private String mobileNumber;

    @Column(length = 10)
    private String gender;

    //newly added
    @Column(length = 20)
    private String role; 
}

