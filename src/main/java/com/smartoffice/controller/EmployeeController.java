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

import com.smartoffice.dto.EmployeeRequest;
import com.smartoffice.dto.EmployeeResponse;
import com.smartoffice.dto.IssueSummaryResponse;
import com.smartoffice.model.Employee;
import com.smartoffice.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public Employee createEmployee( @Valid @RequestBody EmployeeRequest request) {
    	
        Employee employee = new Employee(
                request.getName(),
                request.getEmail(),
                request.getPhone()
        );
    	
        return employeeService.createEmployee(employee);
    }

    @GetMapping
    public List<EmployeeResponse> getAllEmployees() {

        List<Employee> employees = employeeService.getAllEmployees();

        return employees.stream()
                .map(employee -> new EmployeeResponse(
                        employee.getId(),
                        employee.getName(),
                        employee.getEmail(),
                        employee.getPhone(),
                        null
                ))
                .toList();
    }
    
    @GetMapping("/{id}")
    public EmployeeResponse getEmployeeById(@PathVariable Long id) {

        Employee employee = employeeService.getEmployeeById(id);

        List<IssueSummaryResponse> issues = employee.getIssues()
                .stream()
                .map(issue -> new IssueSummaryResponse(
                        issue.getId(),
                        issue.getTitle(),
                        issue.getCategory(),
                        issue.getStatus()
                ))
                .toList();

        return new EmployeeResponse(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getPhone(),
                issues
        );
    }
 
    @PutMapping("/{id}")
    public EmployeeResponse updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {
        Employee employee = new Employee(
                request.getName(),
                request.getEmail(),
                request.getPhone()
        ); 
        Employee updatedEmployee = employeeService.updateEmployee(id, employee);

        return new EmployeeResponse(
                updatedEmployee.getId(),
                updatedEmployee.getName(),
                updatedEmployee.getEmail(),
                updatedEmployee.getPhone(),
                null
        );
    }

    @DeleteMapping("/{id}")
    public EmployeeResponse deleteEmployee(@PathVariable Long id) {

        Employee deletedEmployee = employeeService.deleteEmployee(id);

        return new EmployeeResponse(
                deletedEmployee.getId(),
                deletedEmployee.getName(),
                deletedEmployee.getEmail(),
                deletedEmployee.getPhone(),
                null
        );
    }
   
}