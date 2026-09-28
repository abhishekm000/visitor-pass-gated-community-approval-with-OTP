package com.example.visitorpass.dto;

import com.example.visitorpass.model.ApprovalStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OTPValidationResponse {
    private String message;
    private String visitorName;
    private Long approvalId;
    private LocalDateTime entryTime;
    private ApprovalStatus status;
}
