package com.example.plateformeDons.Controller;

import com.example.plateformeDons.DTO.AnnonceDTO;
import com.example.plateformeDons.Service.FavorisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favoris")
public class FavorisController {

    @Autowired
    private FavorisService favorisService;

    @PostMapping("/ajouter")
    public ResponseEntity<String> ajouterAuxFavoris(@RequestParam Long utilisateurId, @RequestParam Long annonceId) {
        favorisService.ajouterAuxFavoris(utilisateurId, annonceId);
        return ResponseEntity.ok("Annonce ajoutée aux favoris");
    }

    @DeleteMapping("/retirer")
    public ResponseEntity<String> retirerDesFavoris(@RequestParam Long utilisateurId, @RequestParam Long annonceId) {
        favorisService.retirerDesFavoris(utilisateurId, annonceId);
        return ResponseEntity.ok("Annonce retirée des favoris");
    }

    @GetMapping("/{utilisateurId}")
    public ResponseEntity<List<AnnonceDTO>> getFavoris(@PathVariable Long utilisateurId) {
        return ResponseEntity.ok(favorisService.getFavoris(utilisateurId));
    }
}