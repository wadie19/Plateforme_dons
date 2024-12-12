package com.example.plateformeDons.Controller;

import com.example.plateformeDons.Security.Role;
import com.example.plateformeDons.Service.UtilisateurService;
import com.example.plateformeDons.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UtilisateurService userService;

    // API pour créer un utilisateur, accessible uniquement aux administrateurs
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Utilisateur> createUser(@RequestBody Utilisateur user) {
        Utilisateur createdUser = userService.createUser(user);
        return ResponseEntity.status(201).body(createdUser);
    }

    @GetMapping
    public ResponseEntity<List<Utilisateur>> getAllUsers() {
        List<Utilisateur> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // API pour récupérer un utilisateur par son ID, accessible à tous les utilisateurs authentifiés
    //@PreAuthorize("isAuthenticated()")
    @GetMapping("/{id} ")
    public ResponseEntity<Utilisateur> getUserById(@PathVariable Long id) {
        Optional<Utilisateur> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // API pour récupérer un utilisateur par son nom d'utilisateur
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/username/{username}")
    public ResponseEntity<Utilisateur> getUserByUsername(@PathVariable String username) {
        Optional<Utilisateur> user = userService.getUserByUsername(username);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // API pour récupérer un utilisateur par son e-mail
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/email/{email}")
    public ResponseEntity<Utilisateur> getUserByEmail(@PathVariable String email) {
        Optional<Utilisateur> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // API pour mettre à jour un utilisateur, accessible uniquement aux administrateurs ou au propriétaire de l'utilisateur
    //@PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> updateUser(@PathVariable Long id, @RequestBody Utilisateur user) {
        user.setId(id);
        Utilisateur updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    // API pour supprimer un utilisateur, accessible uniquement aux administrateurs ou au propriétaire de l'utilisateur
    //@PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // API pour ajouter un rôle à un utilisateur
    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{userId}/roles")
    public ResponseEntity<Utilisateur> addRoleToUser(@PathVariable Long userId, @RequestBody Role role) {
        Utilisateur updatedUser = userService.addRoleToUser(userId, role);
        return ResponseEntity.ok(updatedUser);
    }

    // API pour retirer un rôle d'un utilisateur
    //@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}/roles")
    public ResponseEntity<Utilisateur> removeRoleFromUser(@PathVariable Long userId, @RequestBody Role role) {
        Utilisateur updatedUser = userService.removeRoleFromUser(userId, role);
        return ResponseEntity.ok(updatedUser);
    }

    // API pour récupérer les rôles d'un utilisateur
    //@PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#userId)")
    @GetMapping("/{userId}/roles")
    public ResponseEntity<Set<Role>> getRolesByUserId(@PathVariable Long userId) {
        Set<Role> roles = userService.getRolesByUserId(userId);
        return ResponseEntity.ok(roles);
    }
}

