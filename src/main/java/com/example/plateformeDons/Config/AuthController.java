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

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTHelper jwtHelper;

    @PostMapping("/register")
    public ResponseEntity<JwtResponse> register(@RequestBody JwtRequest request) {
        Optional<Utilisateur> existingUser = utilisateurService.getUserByEmail(request.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        Utilisateur newUser = new Utilisateur();
        newUser.setEmail(request.getEmail());
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        utilisateurService.createUser(newUser);

        // Générer un token JWT après la création de l'utilisateur
        String token = jwtHelper.generateToken(newUser);

        JwtResponse response = new JwtResponse(token, newUser.getUsername());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        Optional<Utilisateur> user = utilisateurService.getUserByUsername(request.getUsername());

        if (user.isPresent() && passwordEncoder.matches(request.getPassword(), user.get().getPassword())) {
            String token = jwtHelper.generateToken(user.get());

            JwtResponse response = new JwtResponse(token, user.get().getUsername());
            return ResponseEntity.ok(response);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
}
