package com.example.plateformeDons.Repository;

import com.example.plateformeDons.models.Annonce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnonceRepository extends JpaRepository<Annonce, Long>, JpaSpecificationExecutor<Annonce> {
    /*List<Annonce> findByZoneGeographique(String zoneGeographique);

    List<Annonce> findByEtat(String etat);

    @Query("SELECT a FROM Annonce a WHERE :motCle MEMBER OF a.motCles")
    List<Annonce> findByMotCle(@Param("motCle") String motCle);*/

    List<Annonce> findByUtilisateurId(Long utilisateurId);
}
