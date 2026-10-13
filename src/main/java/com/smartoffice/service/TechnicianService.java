package com.smartoffice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smartoffice.exception.TechnicianNotFoundException;
import com.smartoffice.model.Technician;
import com.smartoffice.repository.TechnicianRepository;

@Service
public class TechnicianService {

    private final TechnicianRepository technicianRepository;

    public TechnicianService(TechnicianRepository technicianRepository) {
        this.technicianRepository = technicianRepository;
    }

    public Technician createTechnician(Technician technician) {
        return technicianRepository.save(technician);
    }

    public List<Technician> getAllTechnicians() {
        return technicianRepository.findAll();
    }

    public Technician getTechnicianById(Long id) {
        return technicianRepository.findById(id)
                .orElseThrow(() ->
                    new TechnicianNotFoundException(
                        "Technician with Id " + id + " not found"));
    }

    public Technician updateTechnician(Long id, Technician technician) {

        Technician existingTechnician = technicianRepository.findById(id)
                .orElseThrow(() ->
                    new TechnicianNotFoundException(
                        "Technician with Id " + id + " not found"));

        existingTechnician.setName(technician.getName());
        existingTechnician.setEmail(technician.getEmail());
        existingTechnician.setPhone(technician.getPhone());
        existingTechnician.setSpecialization(technician.getSpecialization());
        existingTechnician.setAvailable(technician.isAvailable());

        return technicianRepository.save(existingTechnician);
    }

    public Technician deleteTechnician(Long id) {

        Technician existingTechnician = technicianRepository.findById(id)
                .orElseThrow(() ->
                    new TechnicianNotFoundException(
                        "Technician with Id " + id + " not found"));

        technicianRepository.delete(existingTechnician);

        return existingTechnician;
    }
}