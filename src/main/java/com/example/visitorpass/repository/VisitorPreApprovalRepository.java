package com.example.visitorpass.repository;

import com.example.visitorpass.model.ApprovalStatus;
import com.example.visitorpass.model.VisitorPreApproval;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VisitorPreApprovalRepository extends JpaRepository<VisitorPreApproval, Long> {
    List<VisitorPreApproval> findByResidentId(Long residentId);
    List<VisitorPreApproval> findByResidentIdAndStatus(Long residentId, ApprovalStatus status);
    List<VisitorPreApproval> findByStatus(ApprovalStatus status);
}
