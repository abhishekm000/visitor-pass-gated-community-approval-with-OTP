package com.example.visitorpass.dto;

import lombok.Data;

@Data
public class FlatResponse {
    private Long id;
    private String flatNumber;
    private String blockName;
    private Integer floorNumber;
    private String status;
}
