package com.example.plateformeDons.Service;

import com.example.plateformeDons.DTO.RechercheDTO;
import com.example.plateformeDons.Repository.RechercheRepository;
import com.example.plateformeDons.models.Recherche;
import com.example.plateformeDons.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RechercheSauvegardeeService {
    @Autowired
    private RechercheRepository rechercheRepository;

    public void sauvegarderRecherche(Utilisateur utilisateur, String zone, String etat, String motCle) {
        try {
            Recherche recherche = new Recherche();
            recherche.setUtilisateur(utilisateur);
            recherche.setZone(zone);
            recherche.setEtat(etat);
            recherche.setMotCle(motCle);

            rechercheRepository.save(recherche);
        } catch (Exception e) {
            throw new RuntimeException("Error while saving the search for utilisateur with ID " + utilisateur.getId(), e);
        }
    }

    // Récupère les recherches non notifiées pour l'utilisateur
    public List<Recherche> getRecherchesNonNotifiees(Long utilisateurId) {
        try {
            return rechercheRepository.findByUtilisateurId(utilisateurId);
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching non-notified searches for utilisateur with ID " + utilisateurId, e);
        }
    }
}
