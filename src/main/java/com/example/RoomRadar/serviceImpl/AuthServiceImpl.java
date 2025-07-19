package com.example.RoomRadar.serviceImpl;

import com.example.RoomRadar.Model.User;
import com.example.RoomRadar.service.Authservice;
import com.example.RoomRadar.Repository.UserRepository;
import com.example.RoomRadar.service.EmailService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements Authservice {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public AuthServiceImpl(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @Override
    public String sendOtp(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with email: " + email);
        }

        String otp = emailService.generateOtp();
        emailService.sendOtpEmail(email, otp);
        emailService.saveOtp(email, otp);

        return "OTP sent to your email.";
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        return emailService.verifyOtp(email, otp);
    }

    @Override
    public String resetPassword(String email,  String newPassword) {


        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        user.setPassword(newPassword); // NO ENCODING used!
        userRepository.save(user);

        return "Password reset successful.";
    }
}
