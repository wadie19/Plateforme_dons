package com.example.plateformeDons.DTO;

public class AnnonceDTO {
    private Long id;
    private String title;
    private String description;
    private String etat;
    private boolean estFavori; // Indique si cette annonce est en favoris pour l'utilisateur connecté
    // Getters et setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public boolean isEstFavori() {
        return estFavori;
    }

    public void setEstFavori(boolean estFavori) {
        this.estFavori = estFavori;
    }
}
