package com.example.plateformeDons.Controller;

import com.example.plateformeDons.DTO.AnnonceDTO;
import com.example.plateformeDons.Service.AnnonceService;
import com.example.plateformeDons.Service.UtilisateurService;
import com.example.plateformeDons.models.Annonce;
import com.example.plateformeDons.models.Utilisateur;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/annonces")
public class AnnonceController {

    @Autowired
    private AnnonceService annonceService;

    @Autowired
    private UtilisateurService utilisateurService;

    // Créer une annonce (accessible uniquement par un utilisateur authentifié)
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Annonce> createAnnonce(@RequestBody Annonce annonce) {
        try {
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = userDetails.getUsername();

            Utilisateur utilisateur = utilisateurService.getUserByUsername(username)
                    .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

            annonce.setUtilisateur(utilisateur);
            Annonce createdAnnonce = annonceService.createAnnonce(annonce);
            return new ResponseEntity<>(createdAnnonce, HttpStatus.CREATED);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Obtenir toutes les annonces
    @GetMapping
    public ResponseEntity<List<Annonce>> getAllAnnonces() {
        List<Annonce> annonces = annonceService.getAllAnnonces();
        return ResponseEntity.ok(annonces);
    }

    // Mettre à jour une annonce (seul le propriétaire ou un administrateur peut le faire)
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwnerOfAnnonce(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<Annonce> updateAnnonce(@PathVariable Long id, @RequestBody Annonce annonce) {
        Annonce updatedAnnonce = annonceService.updateAnnonce(id, annonce);
        return updatedAnnonce != null ? ResponseEntity.ok(updatedAnnonce) : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // Supprimer une annonce (seul le propriétaire ou un administrateur peut le faire)
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwnerOfAnnonce(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnonce(@PathVariable Long id) {
        annonceService.deleteAnnonce(id);
        return ResponseEntity.noContent().build();
    }

    // Rechercher des annonces avec des critères optionnels
    @GetMapping("/recherche")
    public List<Annonce> rechercherAnnonces(
            @RequestParam(required = false) String zone,
            @RequestParam(required = false) String etat,
            @RequestParam(required = false) String motCle) {
        return annonceService.rechercherAnnonces(zone, etat, motCle);
    }

    // Obtenir les annonces d'un utilisateur spécifique
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwnerOfAnnonce(#userId)")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Annonce>> getAnnoncesByUser(@PathVariable Long userId) {
        List<Annonce> annonces = annonceService.getAnnoncesByUser(userId);
        return ResponseEntity.ok(annonces);
    }

    // Obtenir une annonce par son ID
    @GetMapping("/{id}")
    public ResponseEntity<AnnonceDTO> getAnnonceById(@PathVariable Long id) {
        Optional<Annonce> annonce = annonceService.getAnnonceById(id);
        return annonce.map(a -> ResponseEntity.ok(new AnnonceDTO(
                a.getTitle(),
                a.getDescription(),
                a.getEtat(),
                a.getDatePublication(),
                a.getZoneGeographique(),
                a.getModaliteDon()
        ))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
