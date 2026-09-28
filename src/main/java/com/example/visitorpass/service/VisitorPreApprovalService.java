package com.example.visitorpass.service;

import com.example.visitorpass.dto.VisitorPreApprovalRequest;
import com.example.visitorpass.dto.VisitorPreApprovalResponse;
import com.example.visitorpass.exception.BusinessRuleException;
import com.example.visitorpass.exception.InvalidStateException;
import com.example.visitorpass.exception.ResourceNotFoundException;
import com.example.visitorpass.model.ApprovalStatus;
import com.example.visitorpass.model.OTPPass;
import com.example.visitorpass.model.Resident;
import com.example.visitorpass.model.VisitorPreApproval;
import com.example.visitorpass.repository.OTPPassRepository;
import com.example.visitorpass.repository.ResidentRepository;
import com.example.visitorpass.repository.VisitorPreApprovalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class VisitorPreApprovalService {

    private final VisitorPreApprovalRepository visitorPreApprovalRepository;
    private final ResidentRepository residentRepository;
    private final OTPPassRepository otpPassRepository;

    public VisitorPreApprovalService(VisitorPreApprovalRepository visitorPreApprovalRepository,
                                     ResidentRepository residentRepository,
                                     OTPPassRepository otpPassRepository) {
        this.visitorPreApprovalRepository = visitorPreApprovalRepository;
        this.residentRepository = residentRepository;
        this.otpPassRepository = otpPassRepository;
    }

    @Transactional
    public VisitorPreApprovalResponse createPreApproval(VisitorPreApprovalRequest request) {
        Resident resident = residentRepository.findById(request.getResidentId())
                .orElseThrow(() -> new ResourceNotFoundException("Resident not found with id: " + request.getResidentId()));

        if (request.getExpectedVisitDateTime().isBefore(LocalDateTime.now())) {
            throw new BusinessRuleException("Expected visit date time must be in the future.");
        }

        VisitorPreApproval approval = new VisitorPreApproval();
        approval.setResident(resident);
        approval.setVisitorName(request.getVisitorName());
        approval.setVisitorPhone(request.getVisitorPhone());
        approval.setExpectedVisitDateTime(request.getExpectedVisitDateTime());
        approval.setVisitPurpose(request.getVisitPurpose());
        approval.setStatus(ApprovalStatus.PENDING);
        approval.setCreatedAt(LocalDateTime.now());

        // We assume expiration is 2 hours after expected visit time
        LocalDateTime expiresAt = request.getExpectedVisitDateTime().plusHours(2);

        OTPPass otpPass = new OTPPass();
        otpPass.setOtpCode(generateOtp());
        otpPass.setGeneratedAt(LocalDateTime.now());
        otpPass.setExpiresAt(expiresAt);
        otpPass.setUsed(false);
        otpPass.setVisitorPreApproval(approval);

        approval.setOtpPass(otpPass);

        VisitorPreApproval savedApproval = visitorPreApprovalRepository.save(approval);

        VisitorPreApprovalResponse response = mapToResponse(savedApproval);
        response.setOtp(otpPass.getOtpCode());
        response.setMessage("Visitor pre-approval created successfully");
        response.setApprovalId(savedApproval.getId());
        return response;
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public VisitorPreApprovalResponse getPreApprovalById(Long id) {
        updateExpiredStatuses();
        VisitorPreApproval approval = visitorPreApprovalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visitor pre-approval not found with id: " + id));
        return mapToResponse(approval);
    }

    public List<VisitorPreApprovalResponse> getAllPreApprovals() {
        updateExpiredStatuses();
        return visitorPreApprovalRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<VisitorPreApprovalResponse> getResidentPreApprovals(Long residentId) {
        updateExpiredStatuses();
        residentRepository.findById(residentId)
                .orElseThrow(() -> new ResourceNotFoundException("Resident not found with id: " + residentId));
        return visitorPreApprovalRepository.findByResidentId(residentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<VisitorPreApprovalResponse> getPendingPreApprovals(Long residentId) {
        updateExpiredStatuses();
        residentRepository.findById(residentId)
                .orElseThrow(() -> new ResourceNotFoundException("Resident not found with id: " + residentId));
        return visitorPreApprovalRepository.findByResidentIdAndStatus(residentId, ApprovalStatus.PENDING).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public VisitorPreApprovalResponse revokePreApproval(Long id) {
        VisitorPreApproval approval = visitorPreApprovalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visitor pre-approval not found with id: " + id));

        if (approval.getStatus() == ApprovalStatus.USED) {
            throw new InvalidStateException("Cannot revoke an already used pre-approval.");
        }
        if (approval.getStatus() == ApprovalStatus.EXPIRED) {
            throw new InvalidStateException("Cannot revoke an expired pre-approval.");
        }
        if (approval.getStatus() == ApprovalStatus.REVOKED) {
            throw new InvalidStateException("Pre-approval is already revoked.");
        }

        approval.setStatus(ApprovalStatus.REVOKED);
        approval.setRevokedAt(LocalDateTime.now());
        VisitorPreApproval savedApproval = visitorPreApprovalRepository.save(approval);
        return mapToResponse(savedApproval);
    }

    @Transactional
    public void updateExpiredStatuses() {
        List<VisitorPreApproval> pendingApprovals = visitorPreApprovalRepository.findByStatus(ApprovalStatus.PENDING);
        LocalDateTime now = LocalDateTime.now();
        for (VisitorPreApproval approval : pendingApprovals) {
            if (approval.getOtpPass() != null && approval.getOtpPass().getExpiresAt().isBefore(now)) {
                approval.setStatus(ApprovalStatus.EXPIRED);
                visitorPreApprovalRepository.save(approval);
            }
        }
    }

    @Transactional
    public void deletePreApproval(Long id) {
        VisitorPreApproval approval = visitorPreApprovalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Visitor pre-approval not found with id: " + id));
        visitorPreApprovalRepository.delete(approval);
    }

    private VisitorPreApprovalResponse mapToResponse(VisitorPreApproval approval) {
        VisitorPreApprovalResponse response = new VisitorPreApprovalResponse();
        response.setId(approval.getId());
        response.setApprovalId(approval.getId());
        response.setVisitorName(approval.getVisitorName());
        response.setVisitorPhone(approval.getVisitorPhone());
        response.setExpectedVisitDateTime(approval.getExpectedVisitDateTime());
        response.setVisitPurpose(approval.getVisitPurpose());
        response.setStatus(approval.getStatus());
        response.setCreatedAt(approval.getCreatedAt());
        response.setRevokedAt(approval.getRevokedAt());
        response.setEntryTime(approval.getEntryTime());
        return response;
    }
}
