package com.example.RoomRadar.auth;

import com.example.RoomRadar.DTO.ForgotPasswordRequest;
import com.example.RoomRadar.DTO.OtpRequest;
import com.example.RoomRadar.service.Authservice;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final Authservice authService;

    public AuthController(Authservice authService) {
        this.authService = authService;
    }

    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody OtpRequest request) {
        String message = authService.sendOtp(request.getEmail());
        return ResponseEntity.ok(message);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody ForgotPasswordRequest request) {
        boolean isValid = authService.verifyOtp(request.getEmail(), request.getOtp());
        return ResponseEntity.ok(isValid ? "OTP verified." : "Invalid OTP.");
    }

        @PostMapping("/reset-password")
        public ResponseEntity<String> resetPassword(@RequestBody ForgotPasswordRequest request) {
            String message = authService.resetPassword(request.getEmail(),  request.getNewPassword());
            return ResponseEntity.ok(message);
        }
}
