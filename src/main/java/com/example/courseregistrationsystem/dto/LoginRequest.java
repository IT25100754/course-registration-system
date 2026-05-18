package com.example.courseregistrationsystem.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {

    public String username;
    public String password;

    public LoginRequest(){}

    public LoginRequest(String username , String password){
        this.username=username;
        this.password=password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return null;
    }
}

