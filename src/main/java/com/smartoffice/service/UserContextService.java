package com.smartoffice.service;

import com.smartoffice.model.Employee;
import com.smartoffice.model.User;
import com.smartoffice.model.UserRole;
import com.smartoffice.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserContextService {
    private final UserRepository userRepository;
    public UserContextService(UserRepository userRepository) { this.userRepository=userRepository; }
    public User current(Authentication authentication) {
        return userRepository.findByEmailIgnoreCase(authentication.getName()).orElseThrow();
    }
    public Employee currentEmployee(Authentication authentication) { return current(authentication).getEmployee(); }
    public boolean isAdmin(Authentication authentication) { return authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")); }
    public boolean ownsIssue(com.smartoffice.model.Issue issue, Authentication authentication) {
        Employee employee = currentEmployee(authentication);
        return employee != null && issue.getEmployee() != null && issue.getEmployee().getId().equals(employee.getId());
    }
    public UserRole role(Authentication authentication) { return current(authentication).getRole(); }
}
