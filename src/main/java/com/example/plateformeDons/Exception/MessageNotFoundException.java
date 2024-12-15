package com.example.plateformeDons.Exception;

public class MessageNotFoundException extends RuntimeException {
    public MessageNotFoundException(String id) {
        super("Message non trouvé avec l'id: " + id);
    }
}