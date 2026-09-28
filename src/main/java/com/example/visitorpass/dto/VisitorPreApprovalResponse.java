package com.example.visitorpass.dto;

import com.example.visitorpass.model.ApprovalStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VisitorPreApprovalResponse {
    private Long id;
    private String visitorName;
    private String visitorPhone;
    private LocalDateTime expectedVisitDateTime;
    private String visitPurpose;
    private ApprovalStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime revokedAt;
    private LocalDateTime entryTime;
    
    // Sometimes we need to show OTP code especially on creation
    private String otp;
    private Long approvalId;
    private String message;
}
