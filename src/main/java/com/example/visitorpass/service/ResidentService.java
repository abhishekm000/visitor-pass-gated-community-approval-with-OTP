package com.example.visitorpass.service;

import com.example.visitorpass.dto.FlatResponse;
import com.example.visitorpass.dto.ResidentRequest;
import com.example.visitorpass.dto.ResidentResponse;
import com.example.visitorpass.exception.ResourceNotFoundException;
import com.example.visitorpass.model.Flat;
import com.example.visitorpass.model.Resident;
import com.example.visitorpass.repository.FlatRepository;
import com.example.visitorpass.repository.ResidentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResidentService {

    private final ResidentRepository residentRepository;
    private final FlatRepository flatRepository;

    public ResidentService(ResidentRepository residentRepository, FlatRepository flatRepository) {
        this.residentRepository = residentRepository;
        this.flatRepository = flatRepository;
    }

    public ResidentResponse createResident(ResidentRequest request) {
        Flat flat = flatRepository.findById(request.getFlatId())
                .orElseThrow(() -> new ResourceNotFoundException("Flat not found with id: " + request.getFlatId()));

        Resident resident = new Resident();
        resident.setName(request.getName());
        resident.setEmail(request.getEmail());
        resident.setPhone(request.getPhone());
        resident.setFlat(flat);

        Resident savedResident = residentRepository.save(resident);
        return mapToResponse(savedResident);
    }

    public List<ResidentResponse> getAllResidents() {
        return residentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ResidentResponse getResidentById(Long id) {
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resident not found with id: " + id));
        return mapToResponse(resident);
    }

    public void deleteResident(Long id) {
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resident not found with id: " + id));
        residentRepository.delete(resident);
    }

    private ResidentResponse mapToResponse(Resident resident) {
        ResidentResponse response = new ResidentResponse();
        response.setId(resident.getId());
        response.setName(resident.getName());
        response.setEmail(resident.getEmail());
        response.setPhone(resident.getPhone());
        
        FlatResponse flatResponse = new FlatResponse();
        flatResponse.setId(resident.getFlat().getId());
        flatResponse.setFlatNumber(resident.getFlat().getFlatNumber());
        flatResponse.setBlockName(resident.getFlat().getBlockName());
        flatResponse.setFloorNumber(resident.getFlat().getFloorNumber());
        flatResponse.setStatus(resident.getFlat().getStatus());
        
        response.setFlat(flatResponse);
        return response;
    }
}
