package com.example.visitorpass.service;

import com.example.visitorpass.dto.OTPValidationRequest;
import com.example.visitorpass.dto.OTPValidationResponse;
import com.example.visitorpass.exception.BusinessRuleException;
import com.example.visitorpass.exception.ResourceNotFoundException;
import com.example.visitorpass.model.ApprovalStatus;
import com.example.visitorpass.model.OTPPass;
import com.example.visitorpass.model.VisitorPreApproval;
import com.example.visitorpass.repository.OTPPassRepository;
import com.example.visitorpass.repository.VisitorPreApprovalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EntryService {

    private final OTPPassRepository otpPassRepository;
    private final VisitorPreApprovalRepository visitorPreApprovalRepository;

    public EntryService(OTPPassRepository otpPassRepository, VisitorPreApprovalRepository visitorPreApprovalRepository) {
        this.otpPassRepository = otpPassRepository;
        this.visitorPreApprovalRepository = visitorPreApprovalRepository;
    }

    @Transactional
    public OTPValidationResponse validateOTP(OTPValidationRequest request) {
        OTPPass otpPass = otpPassRepository.findByOtpCode(request.getOtp())
                .orElseThrow(() -> new ResourceNotFoundException("OTP not found."));

        VisitorPreApproval approval = otpPass.getVisitorPreApproval();

        if (otpPass.isUsed()) {
            throw new BusinessRuleException("This OTP has already been used.");
        }

        if (approval.getStatus() == ApprovalStatus.REVOKED) {
            throw new BusinessRuleException("This visitor pre-approval has been revoked and cannot be used.");
        }

        if (approval.getStatus() == ApprovalStatus.EXPIRED || otpPass.getExpiresAt().isBefore(LocalDateTime.now())) {
            if (approval.getStatus() != ApprovalStatus.EXPIRED) {
                approval.setStatus(ApprovalStatus.EXPIRED);
                visitorPreApprovalRepository.save(approval);
            }
            throw new BusinessRuleException("This visitor pre-approval has expired and cannot be used.");
        }
        
        if (approval.getStatus() != ApprovalStatus.PENDING) {
            throw new BusinessRuleException("This visitor pre-approval is not in a valid state for entry.");
        }

        LocalDateTime now = LocalDateTime.now();
        
        // Mark OTP as used
        otpPass.setUsed(true);
        otpPass.setUsedAt(now);
        otpPassRepository.save(otpPass);

        // Mark Approval as used
        approval.setStatus(ApprovalStatus.USED);
        approval.setEntryTime(now);
        visitorPreApprovalRepository.save(approval);

        OTPValidationResponse response = new OTPValidationResponse();
        response.setMessage("Entry validated successfully");
        response.setVisitorName(approval.getVisitorName());
        response.setApprovalId(approval.getId());
        response.setEntryTime(approval.getEntryTime());
        response.setStatus(approval.getStatus());

        return response;
    }
}
