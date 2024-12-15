package com.example.plateformeDons.Controller;

import com.example.plateformeDons.Service.NotificationService;
import com.example.plateformeDons.DTO.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Get notifications by user ID
    @GetMapping(value = "/{userId}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUtilisateur(@PathVariable Long userId) {
        List<NotificationDTO> notifications = notificationService.getNotificationsByUtilisateur(userId);
        return ResponseEntity.ok(notifications);
    }

    // Get all notifications
    @GetMapping(value = "", produces = MediaType.ALL_VALUE)
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        List<NotificationDTO> notifications = notificationService.getAllNotifs();
        return ResponseEntity.ok(notifications);
    }

    // Mark a notification as read
    @PutMapping(value = "/read/{notificationId}", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<Void> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        return ResponseEntity.noContent().build();
    }
}
