package com.example.plateformeDons.DTO;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ConversationDTO {
    private Long id;

    @NotNull(message = "title is required")
    private String titre;

    @NotNull(message = "sender 1 ID is required")
    private Long utilisateur1Id;

    @NotNull(message = "receiver 2 ID is required")
    private Long utilisateur2Id;

    @NotNull(message = "Annonce ID is required")
    private Long annonceId;
    private List<MessageDTO> messages;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public Long getUtilisateur1Id() {
        return utilisateur1Id;
    }

    public void setUtilisateur1Id(Long utilisateur1Id) {
        this.utilisateur1Id = utilisateur1Id;
    }

    public Long getUtilisateur2Id() {
        return utilisateur2Id;
    }

    public void setUtilisateur2Id(Long utilisateur2Id) {
        this.utilisateur2Id = utilisateur2Id;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }

    public Long getAnnonceId() {
        return annonceId;
    }

    public void setAnnonceId(Long annonceId) {
        this.annonceId = annonceId;
    }
}
