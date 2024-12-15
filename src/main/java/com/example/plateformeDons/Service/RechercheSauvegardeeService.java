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
            Recherche recherche = new Recherche();
            recherche.setUtilisateur(utilisateur);
            recherche.setZone(zone);
            recherche.setEtat(etat);
            recherche.setMotCle(motCle);

            rechercheRepository.save(recherche);
        }

    // Récupère les recherches non notifiées pour l'utilisateur
    public List<Recherche> getRecherchesNonNotifiees(Long utilisateurId) {
        return rechercheRepository.findByUtilisateurId(utilisateurId);
    }
}
