package com.example.plateformeDons.Repository;

import com.example.plateformeDons.models.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    List<Conversation> findByUtilisateur1IdOrUtilisateur2Id(Long utilisateur1Id, Long utilisateur2Id);

    Conversation findByAnnonceIdAndUtilisateur1Id(Long annonceId, Long utilisateur1Id);

    Optional<Conversation> findById(Long id);

}

