package com.smartoffice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smartoffice.dto.TechnicianRequest;
import com.smartoffice.dto.TechnicianResponse;
import com.smartoffice.model.Technician;
import com.smartoffice.service.TechnicianService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/technicians")
public class TechnicianController {

    private final TechnicianService technicianService;

    public TechnicianController(TechnicianService technicianService) {
        this.technicianService = technicianService;
    }

    @PostMapping
    public TechnicianResponse createTechnician(
            @Valid @RequestBody TechnicianRequest request) {

        Technician technician = new Technician(
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getSpecialization(),
                request.isAvailable()
        );

        Technician savedTechnician =
                technicianService.createTechnician(technician);

        return new TechnicianResponse(
                savedTechnician.getId(),
                savedTechnician.getName(),
                savedTechnician.getEmail(),
                savedTechnician.getPhone(),
                savedTechnician.getSpecialization(),
                savedTechnician.isAvailable()
        );
    }

    @GetMapping
    public List<TechnicianResponse> getAllTechnicians() {

        List<Technician> technicians =
                technicianService.getAllTechnicians();

        return technicians.stream()
                .map(technician -> new TechnicianResponse(
                        technician.getId(),
                        technician.getName(),
                        technician.getEmail(),
                        technician.getPhone(),
                        technician.getSpecialization(),
                        technician.isAvailable()
                ))
                .toList();
    }

    @GetMapping("/{id}")
    public TechnicianResponse getTechnicianById(
            @PathVariable Long id) {

        Technician technician =
                technicianService.getTechnicianById(id);

        return new TechnicianResponse(
                technician.getId(),
                technician.getName(),
                technician.getEmail(),
                technician.getPhone(),
                technician.getSpecialization(),
                technician.isAvailable()
        );
    }

    @PutMapping("/{id}")
    public TechnicianResponse updateTechnician(
            @PathVariable Long id,
            @Valid @RequestBody TechnicianRequest request) {

        Technician technician = new Technician(
                request.getName(),
                request.getEmail(),
                request.getPhone(),
                request.getSpecialization(),
                request.isAvailable()
        );

        Technician updatedTechnician =
                technicianService.updateTechnician(id, technician);

        return new TechnicianResponse(
                updatedTechnician.getId(),
                updatedTechnician.getName(),
                updatedTechnician.getEmail(),
                updatedTechnician.getPhone(),
                updatedTechnician.getSpecialization(),
                updatedTechnician.isAvailable()
        );
    }

    @DeleteMapping("/{id}")
    public TechnicianResponse deleteTechnician(
            @PathVariable Long id) {

        Technician deletedTechnician =
                technicianService.deleteTechnician(id);

        return new TechnicianResponse(
                deletedTechnician.getId(),
                deletedTechnician.getName(),
                deletedTechnician.getEmail(),
                deletedTechnician.getPhone(),
                deletedTechnician.getSpecialization(),
                deletedTechnician.isAvailable()
        );
    }
}