package com.example.plateformeDons.Service;

import com.example.plateformeDons.Repository.NotificationRepository;
import com.example.plateformeDons.DTO.NotificationDTO;
import com.example.plateformeDons.Repository.UtilisateurRepositroy;
import com.example.plateformeDons.models.Annonce;
import com.example.plateformeDons.models.Notification;
import com.example.plateformeDons.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private UtilisateurRepositroy userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    // Méthode pour envoyer une notification
    public void sendNotification(Utilisateur utilisateur, Annonce annonce) {
        try {
            Notification notification = new Notification();
            notification.setUtilisateur(utilisateur);
            notification.setAnnonce(annonce);
            notification.setRead(false);
            notification.setMessage("Une nouvelle annonce correspond à vos critères de recherche !");
            notificationRepository.save(notification);
        } catch (Exception e) {
            throw new RuntimeException("Error while sending notification", e);
        }
    }

    // Méthode pour récupérer les notifications d'un utilisateur
    public List<NotificationDTO> getNotificationsByUtilisateur(Long userId) {
        try {
            Utilisateur utilisateur = getUtilisateurById(userId);
            return notificationRepository.findByUtilisateurId(utilisateur.getId()).stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching notifications for utilisateur with ID " + userId, e);
        }
    }

    // Méthode pour marquer une notification comme lue
    public void markAsRead(Long notificationId) {
        try {
            Notification notification = notificationRepository.findById(notificationId)
                    .orElseThrow(() -> new IllegalArgumentException("Notification introuvable"));
            notification.setRead(true);
            notificationRepository.save(notification);
        } catch (Exception e) {
            throw new RuntimeException("Error while marking notification as read with ID " + notificationId, e);
        }
    }

    // Méthode pour récupérer toutes les notifications
    public List<NotificationDTO> getAllNotifs() {
        try {
            return notificationRepository.findAll().stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching all notifications", e);
        }
    }

    // Méthode pour convertir une entité Notification en DTO
    private NotificationDTO toDto(Notification notification) {
        try {
            NotificationDTO dto = new NotificationDTO();
            dto.setId(notification.getId());
            dto.setUtilisateurId(notification.getUtilisateur().getId());
            dto.setAnnonceId(notification.getAnnonce().getId());
            dto.setMessage(notification.getMessage());
            dto.setRead(notification.isRead());
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Error while converting Notification to DTO", e);
        }
    }

    // Méthode pour récupérer un utilisateur par son ID
    private Utilisateur getUtilisateurById(Long userId) {
        try {
            return userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching utilisateur with ID " + userId, e);
        }
    }
}
