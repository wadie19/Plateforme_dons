package com.example.plateformeDons.Service;

import com.example.plateformeDons.Repository.UtilisateurRepositroy;
import com.example.plateformeDons.Security.Role;
import com.example.plateformeDons.models.Annonce;
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

        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);  // Ajouter le rôle USER par défaut
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public List<Utilisateur> getAllUsers() {
        return userRepository.findAll();
    }


    public Optional<Utilisateur> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<Utilisateur> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<Utilisateur> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Utilisateur updateUser(Utilisateur user){
        return userRepository.save(user);
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    // Ajouter un rôle à un utilisateur
    public Utilisateur addRoleToUser(Long userId, Role role) {
        Optional<Utilisateur> utilisateurOpt = userRepository.findById(userId);
        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            utilisateur.getRoles().add(role); // Ajout du rôle
            return userRepository.save(utilisateur); // Sauvegarde
        }
        throw new IllegalArgumentException("Utilisateur non trouvé");
    }

    // Retirer un rôle à un utilisateur
    public Utilisateur removeRoleFromUser(Long userId, Role role) {
        Optional<Utilisateur> utilisateurOpt = userRepository.findById(userId);
        if (utilisateurOpt.isPresent()) {
            Utilisateur utilisateur = utilisateurOpt.get();
            utilisateur.getRoles().remove(role); // Retrait du rôle
            return userRepository.save(utilisateur); // Sauvegarde
        }
        throw new IllegalArgumentException("Utilisateur non trouvé");
    }

    // Récupérer les rôles d'un utilisateur
    public Set<Role> getRolesByUserId(Long userId) {
        Optional<Utilisateur> utilisateurOpt = userRepository.findById(userId);
        return utilisateurOpt.map(Utilisateur::getRoles).orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
    }

}
