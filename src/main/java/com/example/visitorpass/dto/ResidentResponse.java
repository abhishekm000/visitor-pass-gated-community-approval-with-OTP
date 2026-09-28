package com.example.visitorpass.dto;

import lombok.Data;

@Data
public class ResidentResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private FlatResponse flat;
}
