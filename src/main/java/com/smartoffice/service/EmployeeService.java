package com.smartoffice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.smartoffice.exception.EmployeeNotFoundException;
import com.smartoffice.model.Employee;
import com.smartoffice.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }    
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                    new EmployeeNotFoundException(
                        "Employee with Id " + id + " not found"));
    }
 
    public Employee updateEmployee(Long id, Employee employee) {

        Employee existingEmployee = employeeRepository.findById(id)
        		.orElseThrow(() ->
        	    new EmployeeNotFoundException(
        	        "Employee with Id " + id + " not found"));

        existingEmployee.setName(employee.getName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setPhone(employee.getPhone());

        return employeeRepository.save(existingEmployee);
    }
    
    public Employee deleteEmployee(Long id) {

        Employee existingEmployee = employeeRepository.findById(id)
        		.orElseThrow(() ->
        	    new EmployeeNotFoundException(
        	        "Employee with Id " + id + " not found"));
        employeeRepository.delete(existingEmployee);

        return existingEmployee;
    }
    
}