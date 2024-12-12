package com.example.plateformeDons.Controller;

import com.example.plateformeDons.Service.AnnonceService;
import com.example.plateformeDons.Service.UtilisateurService;
import com.example.plateformeDons.models.Annonce;
import com.example.plateformeDons.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/annonces")
public class AnnonceController {

    @Autowired
    private AnnonceService annonceService;

    @Autowired
    private UtilisateurService utilisateurService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<Annonce> createAnnonce(@RequestBody Annonce annonce) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        Utilisateur utilisateur = utilisateurService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        /*if (!annonce.getUtilisateur().getUsername().equals(username)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }*/

        // Associer l'utilisateur à l'annonce
        annonce.setUtilisateur(utilisateur);

        Annonce createdAnnonce = annonceService.createAnnonce(annonce);

        return new ResponseEntity<>(createdAnnonce, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Annonce>> getAllAnnonces() {
        List<Annonce> annonces = annonceService.getAllAnnonces();
        return ResponseEntity.ok(annonces);
    }


    // API pour mettre à jour une annonce, accessible uniquement au propriétaire de l'annonce ou à un administrateur
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwnerOfAnnonce(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<Annonce> updateAnnonce(@PathVariable Long id, @RequestBody Annonce annonce) {
        Annonce updatedAnnonce = annonceService.updateAnnonce(id, annonce);
        return updatedAnnonce != null ? ResponseEntity.ok(updatedAnnonce)
                                        : ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // API pour supprimer une annonce, accessible uniquement au propriétaire de l'annonce ou à un administrateur
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwnerOfAnnonce(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnonce(@PathVariable Long id) {
        annonceService.deleteAnnonce(id);
        return ResponseEntity.noContent().build();
    }

    /*@GetMapping("/recherche")
    public List<Annonce> rechercherAnnonces(
            @RequestParam(required = false) String zone,
            @RequestParam(required = false) String etat,
            @RequestParam(required = false) String motCle) {
        return annonceService.rechercherAnnonces(zone, etat, motCle);
    }*/

    @GetMapping("/recherche")
    public List<Annonce> rechercherAnnonces(
            @RequestParam(required = false) String zone,
            @RequestParam(required = false) String etat,
            @RequestParam(required = false) String motCle) {
        return annonceService.rechercherAnnonces(zone, etat, motCle);
    }

    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwnerOfAnnonce(#userId)")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Annonce>> getAnnoncesByUser(@PathVariable Long userId) {
        List<Annonce> annonces = annonceService.getAnnoncesByUser(userId);
        return ResponseEntity.ok(annonces);
    }
}
