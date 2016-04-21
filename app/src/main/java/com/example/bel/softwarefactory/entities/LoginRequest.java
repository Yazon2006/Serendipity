package com.example.bel.softwarefactory.entities;

public class LoginRequest {
    UserEntity user;

    public LoginRequest(String email, String password) {
        this.user = new UserEntity (email, password);
    }
}
