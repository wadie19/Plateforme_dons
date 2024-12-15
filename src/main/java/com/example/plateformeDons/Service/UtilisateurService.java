package com.example.plateformeDons.Service;

import com.example.plateformeDons.Repository.UtilisateurRepositroy;
import com.example.plateformeDons.Security.Role;
import com.example.plateformeDons.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepositroy userRepository;

    public Utilisateur createUser(Utilisateur user) {
        try {
            Set<Role> roles = new HashSet<>();
            roles.add(Role.USER);  // Ajouter le rôle USER par défaut
            user.setRoles(roles);

            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user: " + e.getMessage(), e);
        }
    }

    public List<Utilisateur> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving all users: " + e.getMessage(), e);
        }
    }

    public Optional<Utilisateur> getUserById(Long id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user by ID: " + e.getMessage(), e);
        }
    }

    public Optional<Utilisateur> getUserByUsername(String username) {
        try {
            return userRepository.findByUsername(username);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user by username: " + e.getMessage(), e);
        }
    }

    public Optional<Utilisateur> getUserByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving user by email: " + e.getMessage(), e);
        }
    }

    public Utilisateur updateUser(Utilisateur user){
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        }
    }

    public void deleteUser(Long id){
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }

    // Ajouter un rôle à un utilisateur
    public Utilisateur addRoleToUser(Long userId, Role role) {
        try {
            Optional<Utilisateur> utilisateurOpt = userRepository.findById(userId);
            if (utilisateurOpt.isPresent()) {
                Utilisateur utilisateur = utilisateurOpt.get();
                utilisateur.getRoles().add(role); // Ajout du rôle
                return userRepository.save(utilisateur); // Sauvegarde
            }
            throw new IllegalArgumentException("Utilisateur non trouvé");
        } catch (Exception e) {
            throw new RuntimeException("Error adding role to user: " + e.getMessage(), e);
        }
    }

    // Retirer un rôle à un utilisateur
    public Utilisateur removeRoleFromUser(Long userId, Role role) {
        try {
            Optional<Utilisateur> utilisateurOpt = userRepository.findById(userId);
            if (utilisateurOpt.isPresent()) {
                Utilisateur utilisateur = utilisateurOpt.get();
                utilisateur.getRoles().remove(role); // Retrait du rôle
                return userRepository.save(utilisateur); // Sauvegarde
            }
            throw new IllegalArgumentException("Utilisateur non trouvé");
        } catch (Exception e) {
            throw new RuntimeException("Error removing role from user: " + e.getMessage(), e);
        }
    }

    // Récupérer les rôles d'un utilisateur
    public Set<Role> getRolesByUserId(Long userId) {
        try {
            Optional<Utilisateur> utilisateurOpt = userRepository.findById(userId);
            return utilisateurOpt.map(Utilisateur::getRoles).orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
        } catch (Exception e) {
            throw new RuntimeException("Error retrieving roles for user: " + e.getMessage(), e);
        }
    }

}
