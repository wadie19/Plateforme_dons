package com.example.plateformeDons.Repository;

import com.example.plateformeDons.models.Notification;
import com.example.plateformeDons.models.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUtilisateurId(Long utilisateurId);
}

