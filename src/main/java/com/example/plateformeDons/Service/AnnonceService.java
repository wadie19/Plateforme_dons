package com.example.plateformeDons.Service;

import com.example.plateformeDons.Repository.AnnonceRepository;
import com.example.plateformeDons.Repository.RechercheRepository;
import com.example.plateformeDons.Specification.AnnonceSpecification;
import com.example.plateformeDons.models.Annonce;
import com.example.plateformeDons.models.Recherche;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AnnonceService {
    @Autowired
    private AnnonceRepository annonceRepository;
    @Autowired
    private RechercheRepository rechercheRepository;
    @Autowired
    private NotificationService notificationService;

    public Annonce createAnnonce(Annonce annonce) {
        annonce.setDatePublication(LocalDate.now());
        Annonce savedAnnonce = annonceRepository.save(annonce);

        List<Recherche> recherches = rechercheRepository.findAll();
        recherches.stream()
                .filter(recherche -> isSearchMatched(savedAnnonce, recherche))
                .forEach(recherche -> notificationService.sendNotification(recherche.getUtilisateur(), savedAnnonce));

        return savedAnnonce;
    }

    /*private boolean isSearchMatched(Annonce annonce, Recherche recherche) {
        // Vérifiez les correspondances entre les critères de la recherche et l'annonce
        boolean matchesZone = annonce.getZoneGeographique().equalsIgnoreCase(recherche.getZone());
        boolean matchesEtat = annonce.getEtat().equalsIgnoreCase(recherche.getEtat());
        boolean matchesMotCle = annonce.getMotCles().stream()
                .anyMatch(motCle -> motCle.equalsIgnoreCase(recherche.getMotCle()));
        return matchesZone && matchesEtat && matchesMotCle;
    }*/

    private boolean isSearchMatched(Annonce annonce, Recherche recherche) {
        return annonce.getZoneGeographique().equalsIgnoreCase(recherche.getZone())
                && annonce.getEtat().equalsIgnoreCase(recherche.getEtat())
                && annonce.getMotCles().stream()
                .anyMatch(motCle -> motCle.equalsIgnoreCase(recherche.getMotCle()));
    }

    public Optional<Annonce> getAnnonceById(Long id) {
        return annonceRepository.findById(id);
    }

    public List<Annonce> getAllAnnonces() {
        return annonceRepository.findAll();
    }

    public Annonce updateAnnonce(Long id, Annonce annonce) {
        if (annonceRepository.existsById(id)) {
            annonce.setId(id);
            return annonceRepository.save(annonce);
        } else { return null;}
    }

    public void deleteAnnonce(Long id) {
        annonceRepository.deleteById(id);
    }

    /*public List<Annonce> rechercherAnnonces(String zone, String etat, String motCle) {
        if (zone != null) {
            return annonceRepository.findByZoneGeographique(zone);
        }
        if (etat != null) {
            return annonceRepository.findByEtat(etat);
        }
        if (motCle != null) {
            return annonceRepository.findByMotCle(motCle);
        }
        return annonceRepository.findAll();
    }*/

    public List<Annonce> rechercherAnnonces(String zone, String etat, String motCle) {
        return annonceRepository.findAll(AnnonceSpecification.withFilters(zone, etat, motCle));
    }

    public List<Annonce> getAnnoncesByUser(Long userId) {
        return annonceRepository.findByUtilisateurId(userId);
    }
}