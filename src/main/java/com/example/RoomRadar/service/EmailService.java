package com.example.RoomRadar.service;

public interface EmailService {

    String generateOtp();
    void sendOtpEmail(String toEmail, String otp);
    void saveOtp(String email, String otp);

    void sendEmail(String to, String subject, String body);
    // Verify the OTP for given email; returns true if valid and not expired
    boolean verifyOtp(String email, String otp);

    // Check if OTP is verified (valid and not expired)
    boolean isOtpVerified(String email);

    // Clear/delete OTP for the given email after verification or expiry
    void clearOtpForEmail(String email);
}
