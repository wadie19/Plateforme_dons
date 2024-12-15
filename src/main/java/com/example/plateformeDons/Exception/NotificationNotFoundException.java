package com.example.plateformeDons.Exception;

public class NotificationNotFoundException extends RuntimeException {
    public NotificationNotFoundException(String id) {
        super("Notification non trouvée avec l'id: " + id);
    }
}