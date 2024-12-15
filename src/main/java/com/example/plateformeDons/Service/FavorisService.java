package com.example.plateformeDons.Service;

import com.example.plateformeDons.DTO.AnnonceDTO;
import com.example.plateformeDons.Repository.AnnonceRepository;
import com.example.plateformeDons.Repository.UtilisateurRepositroy;
import com.example.plateformeDons.Exception.AnnonceNotFoundException;
import com.example.plateformeDons.Exception.UtilisateurNotFoundException;
import com.example.plateformeDons.Exception.FavorisDuplicateException;
import com.example.plateformeDons.Exception.FavorisNotFoundException;
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

    public void ajouterAuxFavoris(Long utilisateurId, Long annonceId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur introuvable avec l'ID: " + utilisateurId));

        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new AnnonceNotFoundException("Annonce introuvable avec l'ID: " + annonceId));

        if (utilisateur.getFavoris().contains(annonce)) {
            throw new FavorisDuplicateException("Annonce déjà ajoutée aux favoris pour l'utilisateur: " + utilisateurId);
        }

        utilisateur.getFavoris().add(annonce);
        utilisateurRepository.save(utilisateur);
    }

    public void retirerDesFavoris(Long utilisateurId, Long annonceId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur introuvable avec l'ID: " + utilisateurId));

        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new AnnonceNotFoundException("Annonce introuvable avec l'ID: " + annonceId));

        if (!utilisateur.getFavoris().contains(annonce)) {
            throw new FavorisNotFoundException("Annonce introuvable dans les favoris de l'utilisateur: " + utilisateurId);
        }

        utilisateur.getFavoris().remove(annonce);
        utilisateurRepository.save(utilisateur);
    }

    public List<AnnonceDTO> getFavoris(Long utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new UtilisateurNotFoundException("Utilisateur introuvable avec l'ID: " + utilisateurId));

        return utilisateur.getFavoris().stream()
                .map(this::toAnnonceDTO)
                .collect(Collectors.toList());
    }

    private AnnonceDTO toAnnonceDTO(Annonce annonce) {
        AnnonceDTO dto = new AnnonceDTO();
        dto.setId(annonce.getId());
        dto.setTitle(annonce.getTitle());
        dto.setDescription(annonce.getDescription());
        dto.setEtat(annonce.getEtat());
        dto.setEstFavori(true);
        return dto;
    }
}
