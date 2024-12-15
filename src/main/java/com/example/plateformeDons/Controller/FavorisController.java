package com.example.plateformeDons.Controller;

import com.example.plateformeDons.DTO.AnnonceDTO;
import com.example.plateformeDons.Service.FavorisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/favoris")
public class FavorisController {

    @Autowired
    private FavorisService favorisService;

    // Add annonce to favoris
    @PostMapping(value = "/ajouter", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<String> ajouterAuxFavoris(@RequestParam Long annonceId) {
        // Get the currently logged-in user's ID from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // This assumes the username is used as the principal

        // Call service method with the logged-in user's username
        favorisService.ajouterAuxFavoris(currentUsername, annonceId);

        return ResponseEntity.ok("Annonce ajoutée aux favoris");
    }

    // Remove annonce from favoris
    @DeleteMapping(value = "/retirer", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<String> retirerDesFavoris(@RequestParam Long annonceId) {
        // Get the currently logged-in user's ID from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // This assumes the username is used as the principal

        // Call service method with the logged-in user's username
        favorisService.retirerDesFavoris(currentUsername, annonceId);

        return ResponseEntity.ok("Annonce retirée des favoris");
    }

    // Get favoris for the logged-in user
    @GetMapping(value = "/favoris", produces = MediaType.ALL_VALUE)
    public ResponseEntity<List<AnnonceDTO>> getFavoris() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // Get the current logged-in username

        return ResponseEntity.ok(favorisService.getFavorisByUsername(currentUsername));
    }
}
