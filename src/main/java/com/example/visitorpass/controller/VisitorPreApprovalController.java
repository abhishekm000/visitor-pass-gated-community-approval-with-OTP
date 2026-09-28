package com.example.visitorpass.controller;

import com.example.visitorpass.dto.VisitorPreApprovalRequest;
import com.example.visitorpass.dto.VisitorPreApprovalResponse;
import com.example.visitorpass.service.VisitorPreApprovalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pre-approvals")
public class VisitorPreApprovalController {

    private final VisitorPreApprovalService visitorPreApprovalService;

    public VisitorPreApprovalController(VisitorPreApprovalService visitorPreApprovalService) {
        this.visitorPreApprovalService = visitorPreApprovalService;
    }

    @PostMapping
    public ResponseEntity<VisitorPreApprovalResponse> createPreApproval(@Valid @RequestBody VisitorPreApprovalRequest request) {
        VisitorPreApprovalResponse response = visitorPreApprovalService.createPreApproval(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitorPreApprovalResponse> getPreApprovalById(@PathVariable Long id) {
        return ResponseEntity.ok(visitorPreApprovalService.getPreApprovalById(id));
    }

    @GetMapping
    public ResponseEntity<List<VisitorPreApprovalResponse>> getAllPreApprovals() {
        return ResponseEntity.ok(visitorPreApprovalService.getAllPreApprovals());
    }

    @PutMapping("/{id}/revoke")
    public ResponseEntity<VisitorPreApprovalResponse> revokePreApproval(@PathVariable Long id) {
        return ResponseEntity.ok(visitorPreApprovalService.revokePreApproval(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePreApproval(@PathVariable Long id) {
        visitorPreApprovalService.deletePreApproval(id);
        return ResponseEntity.noContent().build();
    }
}
