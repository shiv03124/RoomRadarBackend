package com.example.RoomRadar.auth;

import com.example.RoomRadar.Model.EmailOtp;
import com.example.RoomRadar.Repository.EmailOtpRepository;
import com.example.RoomRadar.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
public class EmailOtpController {

    @Autowired
    private  EmailService emailOtpService;

    
    // Endpoint to request/send OTP to email
    @PostMapping("/send")
    public ResponseEntity<String> sendOtp(@RequestParam String email) {
        String otp = emailOtpService.generateOtp();
        emailOtpService.saveOtp(email, otp);

        try {
            emailOtpService.sendOtpEmail(email, otp); // <-- Call method to send email
            return ResponseEntity.ok("OTP sent successfully to " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send OTP email: " + e.getMessage());
        }
    }


    // Endpoint to verify OTP submitted by user
    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestParam String email, @RequestParam String otp) {
        boolean isValid = emailOtpService.verifyOtp(email, otp);
        if (isValid) {
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired OTP");
        }
    }
}

