package com.example.plateformeDons.Controller;

import com.example.plateformeDons.Security.Role;
import com.example.plateformeDons.Service.UtilisateurService;
import com.example.plateformeDons.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("api/users")
public class UserController {

    @Autowired
    private UtilisateurService userService;

    // Create a new user, only accessible to admins
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<Utilisateur> createUser(@RequestBody Utilisateur user) {
        Utilisateur createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    // Get all users
    @GetMapping(value = "", produces = MediaType.ALL_VALUE)
    public ResponseEntity<List<Utilisateur>> getAllUsers() {
        List<Utilisateur> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // Get a user by ID, accessible only for authenticated users
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/{id}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<Utilisateur> getUserById(@PathVariable Long id) {
        Optional<Utilisateur> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get a user by username, accessible only for authenticated users
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/username/{username}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<Utilisateur> getUserByUsername(@PathVariable String username) {
        Optional<Utilisateur> user = userService.getUserByUsername(username);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get a user by email, accessible only for authenticated users
    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/email/{email}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<Utilisateur> getUserByEmail(@PathVariable String email) {
        Optional<Utilisateur> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Update user details, accessible to admins or the user itself
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#id)")
    @PutMapping(value = "/{id}", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<Utilisateur> updateUser(@PathVariable Long id, @RequestBody Utilisateur user) {
        user.setId(id);
        Utilisateur updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    // Delete user, accessible to admins or the user itself
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#id)")
    @DeleteMapping(value = "/{id}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // Add a role to a user, accessible only to admins
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/{userId}/roles", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<Utilisateur> addRoleToUser(@PathVariable Long userId, @RequestBody Role role) {
        Utilisateur updatedUser = userService.addRoleToUser(userId, role);
        return ResponseEntity.ok(updatedUser);
    }

    // Remove a role from a user, accessible only to admins
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{userId}/roles", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<Utilisateur> removeRoleFromUser(@PathVariable Long userId, @RequestBody Role role) {
        Utilisateur updatedUser = userService.removeRoleFromUser(userId, role);
        return ResponseEntity.ok(updatedUser);
    }

    // Get roles of a user, accessible to admins or the user themselves
    @PreAuthorize("hasRole('ADMIN') or @userSecurity.isOwner(#userId)")
    @GetMapping(value = "/{userId}/roles", produces = MediaType.ALL_VALUE)
    public ResponseEntity<Set<Role>> getRolesByUserId(@PathVariable Long userId) {
        Set<Role> roles = userService.getRolesByUserId(userId);
        return ResponseEntity.ok(roles);
    }
}
