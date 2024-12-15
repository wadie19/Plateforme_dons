package com.example.plateformeDons.Exception;

public class AnnonceNotFoundException extends RuntimeException {
    public AnnonceNotFoundException(String id) {
        super("Annonce non trouvée avec l'id: " + id);
    }
}