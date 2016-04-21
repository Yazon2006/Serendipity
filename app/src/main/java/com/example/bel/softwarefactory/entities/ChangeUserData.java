package com.example.bel.softwarefactory.entities;


public class ChangeUserData {

    private String new_email;
    private String prev_email;
    private String username;

    public ChangeUserData(String new_email, String prev_email, String username) {
        this.new_email = new_email;
        this.prev_email = prev_email;
        this.username = username;
    }

    public void setNew_email(String new_email) {
        this.new_email = new_email;
    }

    public void setPrev_email(String prev_email) {
        this.prev_email = prev_email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNew_email() {
        return new_email;
    }

    public String getPrev_email() {
        return prev_email;
    }

    public String getUsername() {
        return username;
    }
}
