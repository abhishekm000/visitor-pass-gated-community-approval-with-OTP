package com.example.visitorpass.controller;

import com.example.visitorpass.dto.ResidentRequest;
import com.example.visitorpass.dto.ResidentResponse;
import com.example.visitorpass.dto.VisitorPreApprovalResponse;
import com.example.visitorpass.service.ResidentService;
import com.example.visitorpass.service.VisitorPreApprovalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/residents")
public class ResidentController {

    private final ResidentService residentService;
    private final VisitorPreApprovalService visitorPreApprovalService;

    public ResidentController(ResidentService residentService, VisitorPreApprovalService visitorPreApprovalService) {
        this.residentService = residentService;
        this.visitorPreApprovalService = visitorPreApprovalService;
    }

    @PostMapping
    public ResponseEntity<ResidentResponse> createResident(@Valid @RequestBody ResidentRequest request) {
        ResidentResponse response = residentService.createResident(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ResidentResponse>> getAllResidents() {
        return ResponseEntity.ok(residentService.getAllResidents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResidentResponse> getResidentById(@PathVariable Long id) {
        return ResponseEntity.ok(residentService.getResidentById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResident(@PathVariable Long id) {
        residentService.deleteResident(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{residentId}/pre-approvals")
    public ResponseEntity<List<VisitorPreApprovalResponse>> getResidentPreApprovals(@PathVariable Long residentId) {
        return ResponseEntity.ok(visitorPreApprovalService.getResidentPreApprovals(residentId));
    }

    @GetMapping("/{residentId}/pre-approvals/pending")
    public ResponseEntity<List<VisitorPreApprovalResponse>> getPendingPreApprovals(@PathVariable Long residentId) {
        return ResponseEntity.ok(visitorPreApprovalService.getPendingPreApprovals(residentId));
    }
}
