package com.example.bel.softwarefactory.entities;

public class ChangePasswordRequest {
    private String email;
    private String password;
    private String new_password;

    public ChangePasswordRequest(String email, String password, String new_password) {
        this.email = email;
        this.password = password;
        this.new_password = new_password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNew_password() {
        return new_password;
    }
}
