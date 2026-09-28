package com.example.visitorpass.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OTPValidationRequest {
    @NotBlank(message = "OTP must not be blank")
    private String otp;
}
