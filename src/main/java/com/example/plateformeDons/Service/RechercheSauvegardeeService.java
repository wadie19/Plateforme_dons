package com.example.plateformeDons.Service;

import com.example.plateformeDons.Exception.DuplicateRechercheException;
import com.example.plateformeDons.Exception.RechercheNotFoundException;
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
        boolean exists = rechercheRepository.existsByUtilisateurIdAndZoneAndEtatAndMotCle(
                utilisateur.getId(), zone, etat, motCle);

        if (exists) {
            throw new DuplicateRechercheException("Cette recherche existe déjà pour l'utilisateur.");
        }

        Recherche recherche = new Recherche();
        recherche.setUtilisateur(utilisateur);
        recherche.setZone(zone);
        recherche.setEtat(etat);
        recherche.setMotCle(motCle);

        rechercheRepository.save(recherche);
    }

    public List<Recherche> getRecherchesNonNotifiees(Long utilisateurId) {
        List<Recherche> recherches = rechercheRepository.findByUtilisateurId(utilisateurId);
        if (recherches.isEmpty()) {
            throw new RechercheNotFoundException(utilisateurId, "non notifiée");
        }
        return recherches;
    }
}
