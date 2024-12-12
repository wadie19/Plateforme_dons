package com.example.plateformeDons.models;

public class JwtResponse {

    private String jwtToken;
    private String username;

    public JwtResponse(String jwtToken, String username) {
        this.jwtToken = jwtToken;
        this.username = username;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public String getUsername() {
        return username;
    }
}
