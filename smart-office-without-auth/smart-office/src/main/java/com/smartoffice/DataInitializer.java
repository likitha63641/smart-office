package com.smartoffice;

import com.smartoffice.model.Category;
import com.smartoffice.model.Employee;
import com.smartoffice.model.User;
import com.smartoffice.model.UserRole;
import com.smartoffice.repository.CategoryRepository;
import com.smartoffice.repository.EmployeeRepository;
import com.smartoffice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seedData(UserRepository users, EmployeeRepository employees, CategoryRepository categories,
                               PasswordEncoder encoder,
                               @Value("${smartoffice.admin.email:admin@smartoffice.local}") String adminEmail,
                               @Value("${smartoffice.admin.password:Admin@12345}") String adminPassword,
                               @Value("${smartoffice.admin.name:Smart Office Admin}") String adminName,
                               @Value("${smartoffice.admin.phone:9999999999}") String adminPhone) {
        return args -> {
            if (!users.existsByEmailIgnoreCase(adminEmail)) {
                Employee employee = employees.save(new Employee(adminName, adminEmail, adminPhone));
                users.save(new User(adminEmail.toLowerCase(), encoder.encode(adminPassword), UserRole.ADMIN, employee));
            }
            for (String name : List.of("Electrical", "Plumbing", "IT", "HVAC", "Furniture", "Network", "Other")) {
                if (!categories.existsByNameIgnoreCase(name)) categories.save(new Category(name));
            }
        };
    }
}
