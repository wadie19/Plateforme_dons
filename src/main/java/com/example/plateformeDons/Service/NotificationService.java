package com.example.plateformeDons.Service;

import com.example.plateformeDons.Repository.NotificationRepository;
import com.example.plateformeDons.DTO.NotificationDTO;
import com.example.plateformeDons.Repository.UtilisateurRepositroy;
import com.example.plateformeDons.Exception.NotificationNotFoundException;
import com.example.plateformeDons.Exception.NotificationAlreadyReadException;
import com.example.plateformeDons.Exception.UtilisateurNotFoundException;
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

    public void sendNotification(Utilisateur utilisateur, Annonce annonce) {
        Notification notification = new Notification();
        notification.setUtilisateur(utilisateur);
        notification.setAnnonce(annonce);
        notification.setRead(false);
        notification.setMessage("Une nouvelle annonce correspond à vos critères de recherche !");
        notificationRepository.save(notification);
    }

    public List<NotificationDTO> getNotificationsByUtilisateur(Long userId) {
        Utilisateur utilisateur = getUtilisateurById(userId);
        return notificationRepository.findByUtilisateurId(utilisateur.getId()).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void markAsRead(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException("Notification introuvable avec l'ID: " + notificationId));

        if (notification.isRead()) {
            throw new NotificationAlreadyReadException("La notification avec l'ID " + notificationId + " a déjà été lue.");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    public List<NotificationDTO> getAllNotifs() {
        return notificationRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private NotificationDTO toDto(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setUtilisateurId(notification.getUtilisateur().getId());
        dto.setAnnonceId(notification.getAnnonce().getId());
        dto.setMessage(notification.getMessage());
        dto.setRead(notification.isRead());
        return dto;
    }

    private Utilisateur getUtilisateurById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur introuvable avec l'ID: " + userId));
    }
}
