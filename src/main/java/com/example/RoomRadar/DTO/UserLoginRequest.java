package com.example.RoomRadar.DTO;

public class UserLoginRequest {
    private String email;
    private String password;

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email=email;
    }

    public String getPassword(){
        return this.password;
    }

    public void setPassword(String password){
        this.password=password;
    }
}

