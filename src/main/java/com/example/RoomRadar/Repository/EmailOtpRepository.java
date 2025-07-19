package com.example.RoomRadar.Repository;

import com.example.RoomRadar.Model.EmailOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmailOtpRepository extends JpaRepository<EmailOtp, Long> {

    Optional<EmailOtp> findByEmail(String email);

    void deleteByEmail(String email);
}