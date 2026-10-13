package com.smartoffice.controller;

import com.smartoffice.dto.NotificationResponse;
import com.smartoffice.service.NotificationService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    private final NotificationService service;
    public NotificationController(NotificationService service) { this.service=service; }
    @GetMapping
    public List<NotificationResponse> get(Authentication authentication) { return service.getForUser(authentication.getName()); }
    @PutMapping("/{id}/read")
    public Map<String,String> read(@PathVariable Long id, Authentication authentication) {
        service.markRead(id, authentication.getName());
        return Map.of("message", "Notification marked as read");
    }
}
