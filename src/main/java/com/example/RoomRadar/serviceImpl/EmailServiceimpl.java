package com.example.RoomRadar.serviceImpl;

import com.example.RoomRadar.Model.EmailOtp;
import com.example.RoomRadar.Repository.EmailOtpRepository;
import com.example.RoomRadar.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class EmailServiceimpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    EmailOtpRepository otpRepository;

    @Override
    public String generateOtp() {
        Random random = new Random();
        int otpInt = 100000 + random.nextInt(900000);
        return String.valueOf(otpInt);
    }

    @Override
    public void saveOtp(String email, String otp) {
        Optional<EmailOtp> existingOpt = otpRepository.findByEmail(email);

        EmailOtp emailOtp = existingOpt.orElse(new EmailOtp());
        emailOtp.setEmail(email);
        emailOtp.setOtp(otp);
        emailOtp.setRequestedAt(LocalDateTime.now());

        otpRepository.save(emailOtp);
    }

    @Override
    public boolean verifyOtp(String email, String otp) {
        EmailOtp storedOtp = otpRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (!storedOtp.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }


        return true;
    }

    @Override
    public boolean isOtpVerified(String email) {
        Optional<EmailOtp> emailOtpOpt = otpRepository.findByEmail(email);
        if (emailOtpOpt.isEmpty()) return false;

        EmailOtp emailOtp = emailOtpOpt.get();
        return Duration.between(emailOtp.getRequestedAt(), LocalDateTime.now()).toMinutes() <= 10;
    }

    @Override
    public void clearOtpForEmail(String email) {
        otpRepository.deleteByEmail(email);
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            // Set custom name with email address
            helper.setFrom(new InternetAddress("roomradar@example.com", "RoomRadar Support"));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false); // false = plain text, true = HTML

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        } catch (java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Invalid sender name encoding", e);
        }
    }

    @Override
    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your OTP for RoomRadar Registration");
        message.setText("Your OTP is: " + otp + "\n\nIt will expire in 5 minutes.");

        mailSender.send(message);
    }

}
