package com.example.plateformeDons.Controller;

import com.example.plateformeDons.DTO.RechercheDTO;
import com.example.plateformeDons.Service.RechercheSauvegardeeService;
import com.example.plateformeDons.Service.UtilisateurService;
import com.example.plateformeDons.models.Recherche;
import com.example.plateformeDons.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/recherches")
public class RechercheSauvergardeeRestController {

    @Autowired
    private RechercheSauvegardeeService rechercheSauvegardeeService;

    @Autowired
    private UtilisateurService utilisateurService;

    // Save a search
    @PostMapping(value = "/sauvegarder", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<String> sauvegarderRecherche(@RequestParam String zone, @RequestParam String etat, @RequestParam String motCle) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        Utilisateur utilisateur = utilisateurService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        try {
            rechercheSauvegardeeService.sauvegarderRecherche(utilisateur, zone, etat, motCle);
            return ResponseEntity.status(HttpStatus.CREATED).body("Recherche sauvegardée avec succès");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erreur lors de la sauvegarde de la recherche: " + e.getMessage());
        }
    }

    // Get saved searches
    @GetMapping(value = "/list", produces = MediaType.ALL_VALUE)
    public ResponseEntity<List<Recherche>> getSavedSearches() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        Utilisateur utilisateur = utilisateurService.getUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        List<Recherche> savedSearches = rechercheSauvegardeeService.getRecherchesNonNotifiees(utilisateur.getId());
        return ResponseEntity.ok(savedSearches);
    }
}
