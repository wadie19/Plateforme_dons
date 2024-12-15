package com.example.plateformeDons.models;

public class JwtResponse {

    private final String jwtToken;
    private final String username;
    private final Long userId; // Add userId field

    // Update the constructor to include userId
    public JwtResponse(String jwtToken, String username, Long userId) {
        this.jwtToken = jwtToken;
        this.username = username;
        this.userId = userId;
    }

    // Getters
    public String getJwtToken() {
        return jwtToken;
    }

    public String getUsername() {
        return username;
    }

    public Long getUserId() {
        return userId;
    }
}
