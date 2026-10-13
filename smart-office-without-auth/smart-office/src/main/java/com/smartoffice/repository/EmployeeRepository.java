package com.smartoffice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartoffice.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}