package com.example.RoomRadar.DTO;

import lombok.Getter;
import lombok.Setter;


public class GoogleSignInRequest {

    private String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
