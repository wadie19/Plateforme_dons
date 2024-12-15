package com.example.plateformeDons.Exception;

public class RechercheNotFoundException extends RuntimeException {
    public RechercheNotFoundException(String id) {
        super("Recherche sauvegardée non trouvée avec l'id: " + id);
    }

    public RechercheNotFoundException(Long utilisateurId, String criteres) {
        super(String.format("Aucune recherche trouvée pour l'utilisateur %d avec les critères: %s", utilisateurId, criteres));
    }
}