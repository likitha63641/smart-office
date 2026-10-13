package com.smartoffice.service;

import com.smartoffice.dto.NotificationResponse;
import com.smartoffice.model.Notification;
import com.smartoffice.model.User;
import com.smartoffice.model.UserRole;
import com.smartoffice.repository.NotificationRepository;
import com.smartoffice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    public void create(String email, Long issueId, String message) {
        userRepository.findByEmailIgnoreCase(email)
                .ifPresent(user -> notificationRepository.save(new Notification(user, issueId, message)));
    }

    public void notifyAdmins(Long issueId, String message) {
        String msg = message.length() > 500 ? message.substring(0, 497) + "..." : message;
        userRepository.findByRole(UserRole.ADMIN)
                .forEach(admin -> notificationRepository.save(new Notification(admin, issueId, msg)));
    }

    public List<NotificationResponse> getForUser(String email) {
        User user = userRepository.findByEmailIgnoreCase(email).orElseThrow();
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .map(n -> new NotificationResponse(n.getId(), n.getIssueId(), n.getMessage(), n.isRead(), n.getCreatedAt())).toList();
    }

    @Transactional
    public void markRead(Long id, String email) {
        User user = userRepository.findByEmailIgnoreCase(email).orElseThrow();
        notificationRepository.findById(id)
                .filter(n -> n.getUser().getId().equals(user.getId()))
                .ifPresent(n -> { n.setRead(true); });
    }
}