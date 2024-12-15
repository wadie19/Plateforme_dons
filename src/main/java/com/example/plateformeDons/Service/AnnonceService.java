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
        try {
            annonce.setDatePublication(LocalDate.now());
            Annonce savedAnnonce = annonceRepository.save(annonce);

            List<Recherche> recherches = rechercheRepository.findAll();
            recherches.stream()
                    .filter(recherche -> isSearchMatched(savedAnnonce, recherche))
                    .forEach(recherche -> notificationService.sendNotification(recherche.getUtilisateur(), savedAnnonce));

            return savedAnnonce;
        } catch (Exception e) {
            throw new RuntimeException("Error while creating the annonce", e);
        }
    }

    private boolean isSearchMatched(Annonce annonce, Recherche recherche) {
        return annonce.getZoneGeographique().equalsIgnoreCase(recherche.getZone())
                && annonce.getEtat().equalsIgnoreCase(recherche.getEtat())
                && annonce.getMotCles().stream()
                .anyMatch(motCle -> motCle.equalsIgnoreCase(recherche.getMotCle()));
    }

    public Optional<Annonce> getAnnonceById(Long id) {
        try {
            return annonceRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching the annonce by ID", e);
        }
    }

    public List<Annonce> getAllAnnonces() {
        try {
            return annonceRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching all annonces", e);
        }
    }

    public Annonce updateAnnonce(Long id, Annonce annonce) {
        try {
            if (annonceRepository.existsById(id)) {
                annonce.setId(id);
                return annonceRepository.save(annonce);
            } else {
                throw new RuntimeException("Annonce with ID " + id + " does not exist.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while updating the annonce", e);
        }
    }

    public void deleteAnnonce(Long id) {
        try {
            if (annonceRepository.existsById(id)) {
                annonceRepository.deleteById(id);
            } else {
                throw new RuntimeException("Annonce with ID " + id + " does not exist.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting the annonce", e);
        }
    }

    public List<Annonce> rechercherAnnonces(String zone, String etat, String motCle) {
        try {
            return annonceRepository.findAll(AnnonceSpecification.withFilters(zone, etat, motCle));
        } catch (Exception e) {
            throw new RuntimeException("Error while searching for annonces", e);
        }
    }

    public List<Annonce> getAnnoncesByUser(Long userId) {
        try {
            return annonceRepository.findByUtilisateurId(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error while fetching annonces for user with ID " + userId, e);
        }
    }
}
