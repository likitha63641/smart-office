package com.smartoffice.service;

import com.smartoffice.dto.AuthResponse;
import com.smartoffice.dto.LoginRequest;
import com.smartoffice.dto.ProfileResponse;
import com.smartoffice.dto.ProfileUpdateRequest;
import com.smartoffice.dto.RegisterRequest;
import com.smartoffice.model.Employee;
import com.smartoffice.model.User;
import com.smartoffice.model.UserRole;
import com.smartoffice.repository.EmployeeRepository;
import com.smartoffice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, EmployeeRepository employeeRepository,
                       PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository=userRepository; this.employeeRepository=employeeRepository;
        this.passwordEncoder=passwordEncoder; this.authenticationManager=authenticationManager; this.jwtService=jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new IllegalStateException("An account already exists for this email");
        }
        Employee employee = employeeRepository.save(new Employee(request.getName(), request.getEmail(), request.getPhone()));
        User user = new User(request.getEmail().trim().toLowerCase(), passwordEncoder.encode(request.getPassword()), UserRole.USER, employee);
        user = userRepository.save(user);
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        return new AuthResponse("Registration successful", token, user.getRole().name(), employee.getId(), employee.getName(), user.getEmail());
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        User user = userRepository.findByEmailIgnoreCase(request.getEmail()).orElseThrow();
        Employee employee = user.getEmployee();
        String name = employee != null ? employee.getName() : user.getEmail();
        Long employeeId = employee != null ? employee.getId() : null;
        String token = jwtService.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        return new AuthResponse("Login successful", token, user.getRole().name(), employeeId, name, user.getEmail());
    }

    public ProfileResponse profile(String email) {
        User user = userRepository.findByEmailIgnoreCase(email).orElseThrow();
        Employee employee = user.getEmployee();
        return new ProfileResponse(user.getId(), employee != null ? employee.getId() : null,
                employee != null ? employee.getName() : user.getEmail(), user.getEmail(),
                employee != null ? employee.getPhone() : null, user.getRole().name());
    }
    @Transactional
    public ProfileResponse updateProfile(String email, ProfileUpdateRequest request) {
        User user = userRepository.findByEmailIgnoreCase(email).orElseThrow();
        if (user.getEmployee() == null) throw new IllegalStateException("This account has no employee profile");
        user.getEmployee().setName(request.getName().trim());
        user.getEmployee().setPhone(request.getPhone().trim());
        employeeRepository.save(user.getEmployee());
        return profile(email);
    }

}
