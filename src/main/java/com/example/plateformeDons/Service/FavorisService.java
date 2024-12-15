package com.example.plateformeDons.Service;

import com.example.plateformeDons.DTO.AnnonceDTO;
import com.example.plateformeDons.Repository.AnnonceRepository;
import com.example.plateformeDons.Repository.UtilisateurRepositroy;
import com.example.plateformeDons.models.Annonce;
import com.example.plateformeDons.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavorisService {
    @Autowired
    private UtilisateurRepositroy utilisateurRepository;

    @Autowired
    private AnnonceRepository annonceRepository;

    public void ajouterAuxFavoris(String username, Long annonceId) {
        try {
            Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

            Annonce annonce = annonceRepository.findById(annonceId)
                    .orElseThrow(() -> new IllegalArgumentException("Annonce introuvable"));

            if (!utilisateur.getFavoris().contains(annonce)) {
                utilisateur.getFavoris().add(annonce);
                utilisateurRepository.save(utilisateur);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while adding annonce to favoris", e);
        }
    }

    public void retirerDesFavoris(String username, Long annonceId) {
        try {
            // Fetch the user based on the username
            Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

            Annonce annonce = annonceRepository.findById(annonceId)
                    .orElseThrow(() -> new IllegalArgumentException("Annonce introuvable"));

            if (utilisateur.getFavoris().contains(annonce)) {
                utilisateur.getFavoris().remove(annonce);
                utilisateurRepository.save(utilisateur);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while removing annonce from favoris", e);
        }
    }

    public List<AnnonceDTO> getFavorisByUsername(String username) {
        try {
            // Fetch the user based on the username
            Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalArgumentException("Utilisateur introuvable"));

            // Convert and return the favoris as DTOs
            return utilisateur.getFavoris().stream()
                    .map(this::toAnnonceDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching favoris for utilisateur with username " + username, e);
        }
    }

    private AnnonceDTO toAnnonceDTO(Annonce annonce) {
        try {
            AnnonceDTO dto = new AnnonceDTO();
            dto.setId(annonce.getId());
            dto.setTitle(annonce.getTitle());
            dto.setDescription(annonce.getDescription());
            dto.setEtat(annonce.getEtat());
            dto.setEstFavori(true); // Toujours true ici car ce sont les favoris
            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Error while converting Annonce to DTO", e);
        }
    }
}
