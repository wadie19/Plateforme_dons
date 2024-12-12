package com.example.plateformeDons.DTO;

public class RechercheDTO {
    private String zone;
    private String etat;
    private String motCle;

    // Constructeurs
    public RechercheDTO() {
    }

    public RechercheDTO(String zone, String etat, String motCle) {
        this.zone = zone;
        this.etat = etat;
        this.motCle = motCle;
    }

    // Getters et Setters
    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getMotCle() {
        return motCle;
    }

    public void setMotCle(String motCle) {
        this.motCle = motCle;
    }

    @Override
    public String toString() {
        return "RechercheSauvegardeeDTO{" +
                "zone='" + zone + '\'' +
                ", etat='" + etat + '\'' +
                ", motCle='" + motCle + '\'' +
                '}';
    }
}
