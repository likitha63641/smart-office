package com.smartoffice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartoffice.model.Technician;

public interface TechnicianRepository extends JpaRepository<Technician, Long> {

}