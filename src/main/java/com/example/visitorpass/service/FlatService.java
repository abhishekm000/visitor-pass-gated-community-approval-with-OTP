package com.example.visitorpass.service;

import com.example.visitorpass.dto.FlatRequest;
import com.example.visitorpass.dto.FlatResponse;
import com.example.visitorpass.exception.ResourceNotFoundException;
import com.example.visitorpass.model.Flat;
import com.example.visitorpass.repository.FlatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlatService {
    
    private final FlatRepository flatRepository;

    public FlatService(FlatRepository flatRepository) {
        this.flatRepository = flatRepository;
    }

    public FlatResponse createFlat(FlatRequest request) {
        Flat flat = new Flat();
        flat.setFlatNumber(request.getFlatNumber());
        flat.setBlockName(request.getBlockName());
        flat.setFloorNumber(request.getFloorNumber());
        Flat savedFlat = flatRepository.save(flat);
        return mapToResponse(savedFlat);
    }

    public List<FlatResponse> getAllFlats() {
        return flatRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public FlatResponse getFlatById(Long id) {
        Flat flat = flatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flat not found with id: " + id));
        return mapToResponse(flat);
    }

    public void deleteFlat(Long id) {
        Flat flat = flatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flat not found with id: " + id));
        flatRepository.delete(flat);
    }

    private FlatResponse mapToResponse(Flat flat) {
        FlatResponse response = new FlatResponse();
        response.setId(flat.getId());
        response.setFlatNumber(flat.getFlatNumber());
        response.setBlockName(flat.getBlockName());
        response.setFloorNumber(flat.getFloorNumber());
        response.setStatus(flat.getStatus());
        return response;
    }
}
