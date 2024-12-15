package com.example.plateformeDons.Exception;

public class DuplicateUserException extends RuntimeException {
    public DuplicateUserException(String field, String value) {
        super(String.format("Un utilisateur existe déjà avec %s: %s", field, value));
    }
}