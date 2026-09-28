package com.example.visitorpass.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_passes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OTPPass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String otpCode;

    @Column(nullable = false, updatable = false)
    private LocalDateTime generatedAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private boolean used = false;

    private LocalDateTime usedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "visitor_pre_approval_id", nullable = false)
    private VisitorPreApproval visitorPreApproval;
}
