package com.example.plateformeDons.Exception;

public class FavorisNotFoundException extends RuntimeException {
    public FavorisNotFoundException(String utilisateurId) {
        super("Aucun favori trouvé pour l'utilisateur avec l'id: " + utilisateurId);
    }
}