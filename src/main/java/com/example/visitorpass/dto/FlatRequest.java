package com.example.visitorpass.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FlatRequest {
    @NotBlank(message = "Flat number must not be blank")
    private String flatNumber;
    
    private String blockName;
    private Integer floorNumber;
}
