package com.example.visitorpass.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "visitor_pre_approvals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VisitorPreApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String visitorName;

    @Column(nullable = false)
    private String visitorPhone;

    @Column(nullable = false)
    private LocalDateTime expectedVisitDateTime;

    @Column(nullable = false)
    private String visitPurpose;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus status = ApprovalStatus.PENDING;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime revokedAt;

    private LocalDateTime entryTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resident_id", nullable = false)
    private Resident resident;

    @OneToOne(mappedBy = "visitorPreApproval", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private OTPPass otpPass;
}
