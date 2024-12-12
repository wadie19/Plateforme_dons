package com.example.plateformeDons.Security;

import com.example.plateformeDons.models.Utilisateur;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

@Component
public class JWTHelper {

    // Clé secrète pour signer le token JWT
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    // Durée d'expiration du token (en millisecondes)
    @Value("${jwt.expiration-time}") // Assurez-vous que cette propriété est définie dans votre application.properties
    private long EXPIRATION_TIME;

    // Générer un token JWT pour un utilisateur
    public String generateToken(Utilisateur user) {
        try {
            return Jwts.builder()
                    .setSubject(user.getUsername())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Utilisation de la variable configurable
                    .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Utilisation de la clé secrète injectée
                    .compact();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generating token: " + e.getMessage());
        }
    }

    // Extraire le nom d'utilisateur du token JWT
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Vérifier si le token JWT est valide
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token has expired.");
        } catch (UnsupportedJwtException e) {
            System.out.println("Unsupported JWT token.");
        } catch (MalformedJwtException e) {
            System.out.println("Malformed JWT token.");
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature.");
        } catch (Exception e) {
            System.out.println("Token validation failed.");
        }
        return false;
    }

    // Extraire le token JWT de la requête HTTP
    public String getTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    // Extraire les détails de l'utilisateur à partir du token JWT
    public User getUserDetailsFromToken(String token) {
        String username = getUsernameFromToken(token);
        return new User(username, "", new ArrayList<>());
    }
}
