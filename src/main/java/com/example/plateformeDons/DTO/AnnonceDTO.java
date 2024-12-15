package com.example.plateformeDons.DTO;

import java.time.LocalDate;

public class AnnonceDTO {

    private String title;
    private String description;
    private String etat;
    private LocalDate datePublication;
    private String zoneGeographique;
    private String modaliteDon;
    private Long id;
    private boolean estFavori; 


      public AnnonceDTO( ) {
       
    }
    public AnnonceDTO(String title, String description, String etat, LocalDate datePublication, String zoneGeographique, String modaliteDon) {
        this.title = title;
        this.description = description;
        this.etat = etat;
        this.datePublication = datePublication;
        this.zoneGeographique = zoneGeographique;
        this.modaliteDon = modaliteDon;
    }


    // Getters and Setters
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

    public LocalDate getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(LocalDate datePublication) {
        this.datePublication = datePublication;
    }

    public String getZoneGeographique() {
        return zoneGeographique;
    }

    public void setZoneGeographique(String zoneGeographique) {
        this.zoneGeographique = zoneGeographique;
    }

    public String getModaliteDon() {
        return modaliteDon;
    }

    public void setModaliteDon(String modaliteDon) {
        this.modaliteDon = modaliteDon;

    }


      public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

     public boolean isEstFavori() {
        return estFavori;
    }
    public void setEstFavori(boolean b) {
 this.estFavori = b;    }
}
