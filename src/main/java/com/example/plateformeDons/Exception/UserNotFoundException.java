package com.example.plateformeDons.Exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("Utilisateur non trouvé avec l'id: " + id);
    }

    public UserNotFoundException(String identifier, String type) {
        super(String.format("Utilisateur non trouvé avec %s: %s", type, identifier));
    }
}