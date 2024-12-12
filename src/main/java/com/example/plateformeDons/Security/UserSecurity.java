package com.example.plateformeDons.Security;

import com.example.plateformeDons.Service.UtilisateurService;
import com.example.plateformeDons.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Component
public class UserSecurity {

    @Autowired
    private UtilisateurService utilisateurService;

    // Méthode qui vérifie si l'utilisateur actuel (connecté) est le propriétaire de l'utilisateur avec l'ID fourni
    public boolean isOwner(Long userId) {
        // Récupérer l'utilisateur authentifié à partir du contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Récupérer l'utilisateur correspondant au nom d'utilisateur actuel
        Utilisateur user = utilisateurService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifier si l'utilisateur authentifié est bien celui avec l'ID fourni
        return user.getId().equals(userId);
    }

    public boolean isOwnerOfAnnonce(Long annonceId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Utilisateur utilisateur = utilisateurService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifier que l'utilisateur est bien le propriétaire de l'annonce
        return utilisateur.getAnnonces().stream()
                .anyMatch(annonce -> annonce.getId().equals(annonceId));
    }
}
