package com.example.visitorpass.repository;

import com.example.visitorpass.model.OTPPass;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OTPPassRepository extends JpaRepository<OTPPass, Long> {
    Optional<OTPPass> findByOtpCode(String otpCode);
}
