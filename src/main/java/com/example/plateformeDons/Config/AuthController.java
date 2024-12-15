package com.example.plateformeDons.Config;

import com.example.plateformeDons.Security.JWTHelper;
import com.example.plateformeDons.Service.UtilisateurService;
import com.example.plateformeDons.models.JwtRequest;
import com.example.plateformeDons.models.JwtResponse;
import com.example.plateformeDons.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Optional;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTHelper jwtHelper;

    // Registration endpoint
    @PostMapping(value = "/register", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<?> register(@RequestBody JwtRequest request) {
        try {
            // Check if email or username already exists
            Optional<Utilisateur> existingUserByEmail = utilisateurService.getUserByEmail(request.getEmail());
            if (existingUserByEmail.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already in use.");
            }

            Optional<Utilisateur> existingUserByUsername = utilisateurService.getUserByUsername(request.getUsername());
            if (existingUserByUsername.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already in use.");
            }

            // Create new user
            Utilisateur newUser = new Utilisateur();
            newUser.setEmail(request.getEmail());
            newUser.setUsername(request.getUsername());
            newUser.setPassword(passwordEncoder.encode(request.getPassword()));

            utilisateurService.createUser(newUser);  // Make sure this persists the user

            // Generate a JWT token after creating the user
            String token = jwtHelper.generateToken(newUser);

            // Respond with the generated token and username
            JwtResponse response = new JwtResponse(token, newUser.getUsername(), newUser.getId());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during registration: " + e.getMessage());
        }
    }

    @PostMapping(value = "/login", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<?> login(@RequestBody JwtRequest request) {
        try {
            // Find user by username
            Optional<Utilisateur> user = utilisateurService.getUserByUsername(request.getUsername());

            // Validate password and generate JWT token
            if (user.isPresent() && passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
                String token = jwtHelper.generateToken(user.get());

                // Respond with JWT, username, and user ID
                JwtResponse response = new JwtResponse(token, user.get().getUsername(), user.get().getId());
                return ResponseEntity.ok(response);
            }

            // Invalid credentials
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login: " + e.getMessage());
        }
    }

}
