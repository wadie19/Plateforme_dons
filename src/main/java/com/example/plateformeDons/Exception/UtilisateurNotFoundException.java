package com.example.plateformeDons.Exception;

public class UtilisateurNotFoundException extends RuntimeException {
    public UtilisateurNotFoundException(String username) {
        super("Utilisateur non trouvé avec le username: " + username);
    }

    public UtilisateurNotFoundException(Long id) {
        super("Utilisateur non trouvé avec l'id: " + id);
    }
}