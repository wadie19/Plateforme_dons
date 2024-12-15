package com.example.plateformeDons.Exception;

public class ConversationNotFoundException extends RuntimeException {
    public ConversationNotFoundException(String id) {
        super("Conversation non trouvée avec l'id: " + id);
    }
}