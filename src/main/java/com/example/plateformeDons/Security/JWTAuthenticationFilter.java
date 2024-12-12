package com.example.plateformeDons.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
    private final JWTHelper jwtHelper;

    public JWTAuthenticationFilter(JWTHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtHelper.getTokenFromRequest(request); // Récupérer le token à partir de la requête
        if (token != null && jwtHelper.validateToken(token)) { // Vérifier si le token est valide
            try {
                // Obtient les détails de l'utilisateur à partir du token
                var userDetails = jwtHelper.getUserDetailsFromToken(token);

                // Crée un objet Authentication pour Spring Security
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Enregistre l'authentification dans le SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            } catch (ExpiredJwtException | MalformedJwtException ex) {
                logger.error("Token expired or invalid: " + ex.getMessage());
            }
        }
        filterChain.doFilter(request, response);
    }
}
