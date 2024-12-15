package com.example.plateformeDons.Exception;

public class NotificationAlreadyReadException extends RuntimeException {
    public NotificationAlreadyReadException(String id) {
        super("La notification avec l'id: " + id + " est déjà marquée comme lue");
    }
}