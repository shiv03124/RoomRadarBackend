package com.example.RoomRadar.service;

public interface Authservice {
    String sendOtp(String email);
    boolean verifyOtp(String email, String otp);
    String resetPassword(String email,  String newPassword);
}
