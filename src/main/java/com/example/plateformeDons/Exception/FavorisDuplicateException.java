package com.example.plateformeDons.Exception;

public class FavorisDuplicateException extends RuntimeException {
    public FavorisDuplicateException(String annonceId) {
        super("L'annonce avec l'id: " + annonceId + " est déjà dans les favoris");
    }
}