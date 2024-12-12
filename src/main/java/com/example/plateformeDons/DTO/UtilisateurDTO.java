package com.example.plateformeDons.DTO;

import java.util.List;

public class UtilisateurDTO {
    private Long id;
    private String username;
    private String email;
    private List<Long> favoris;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Long> getFavoris() {
        return favoris;
    }

    public void setFavoris(List<Long> favoris) {
        this.favoris = favoris;
    }
}
