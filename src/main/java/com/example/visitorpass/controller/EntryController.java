package com.example.visitorpass.controller;

import com.example.visitorpass.dto.OTPValidationRequest;
import com.example.visitorpass.dto.OTPValidationResponse;
import com.example.visitorpass.service.EntryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/entry")
public class EntryController {

    private final EntryService entryService;

    public EntryController(EntryService entryService) {
        this.entryService = entryService;
    }

    @PostMapping("/validate")
    public ResponseEntity<OTPValidationResponse> validateOTP(@Valid @RequestBody OTPValidationRequest request) {
        OTPValidationResponse response = entryService.validateOTP(request);
        return ResponseEntity.ok(response);
    }
}
