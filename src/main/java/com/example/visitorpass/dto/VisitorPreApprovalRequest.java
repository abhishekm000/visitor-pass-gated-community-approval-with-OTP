package com.example.visitorpass.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class VisitorPreApprovalRequest {

    @NotNull(message = "Resident ID must be provided")
    private Long residentId;

    @NotBlank(message = "Visitor name must not be blank")
    private String visitorName;

    @NotBlank(message = "Visitor phone must not be blank")
    private String visitorPhone;

    @NotNull(message = "Expected visit date time must be provided")
    private LocalDateTime expectedVisitDateTime;

    @NotBlank(message = "Visit purpose must not be blank")
    private String visitPurpose;
}
