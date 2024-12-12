package com.example.plateformeDons.Repository;

import com.example.plateformeDons.models.Recherche;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RechercheRepository extends JpaRepository<Recherche, Long> {

    List<Recherche> findByUtilisateurId(Long utilisateurId);
}
