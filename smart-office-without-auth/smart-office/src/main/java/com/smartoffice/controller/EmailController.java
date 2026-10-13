package com.smartoffice.controller;

import com.smartoffice.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/notifications")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/email")
    public ResponseEntity<?> sendEmail(
            @RequestBody Map<String, String> request) {

        String to = request.get("to");
        String subject = request.get("subject");
        String body = request.get("body");

        if (to == null || to.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", 400,
                            "message", "Recipient email is required"
                    )
            );
        }

        if (subject == null || subject.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", 400,
                            "message", "Email subject is required"
                    )
            );
        }

        if (body == null || body.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "status", 400,
                            "message", "Email message is required"
                    )
            );
        }

        try {

            emailService.sendEmail(
                    to,
                    subject,
                    body
            );

            return ResponseEntity.ok(
                    Map.of(
                            "status", 200,
                            "message", "Email sent successfully"
                    )
            );

        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(
                    Map.of(
                            "status", 500,
                            "message", "Failed to send email"
                    )
            );
        }
    }
}