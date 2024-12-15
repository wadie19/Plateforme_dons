package com.example.plateformeDons.Service;

import com.example.plateformeDons.DTO.ConversationDTO;
import com.example.plateformeDons.DTO.MessageDTO;
import com.example.plateformeDons.Exception.ConversationNotFoundException;
import com.example.plateformeDons.Exception.MessageNotFoundException;
import com.example.plateformeDons.Repository.ConversationRepository;
import com.example.plateformeDons.Repository.MessageRepository;
import com.example.plateformeDons.models.Annonce;
import com.example.plateformeDons.models.Conversation;
import com.example.plateformeDons.models.Message;
import com.example.plateformeDons.models.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConversationService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    public ConversationDTO createConversation(ConversationDTO conversationDTO) {
        Utilisateur utilisateur1 = new Utilisateur();
        utilisateur1.setId(conversationDTO.getUtilisateur1Id());

        Utilisateur utilisateur2 = new Utilisateur();
        utilisateur2.setId(conversationDTO.getUtilisateur2Id());

        Annonce annonce = new Annonce();
        annonce.setId(conversationDTO.getAnnonceId());

        Conversation existing = conversationRepository.findByAnnonceIdAndUtilisateur1Id(annonce.getId(), utilisateur1.getId());
        if (existing != null) {
            return toDto(existing);
        }

        Conversation conversation = new Conversation();
        conversation.setUtilisateur1(utilisateur1);
        conversation.setUtilisateur2(utilisateur2);
        conversation.setAnnonce(annonce);
        conversation.setTitre(annonce.getTitle());

        return toDto(conversationRepository.save(conversation));
    }

    public List<ConversationDTO> getConversationsForUser(Long userId) {
        List<Conversation> conversations = conversationRepository.findByUtilisateur1IdOrUtilisateur2Id(userId, userId);

        return conversations.stream()
                .map(conversation -> {
                    ConversationDTO dto = toDto(conversation);
                    List<MessageDTO> messages = conversation.getMessages().stream()
                            .map(this::toMessageDto)
                            .collect(Collectors.toList());
                    dto.setMessages(messages);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public ConversationDTO getConversationById(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException("Conversation not found with ID: " + conversationId));

        ConversationDTO dto = toDto(conversation);

        List<MessageDTO> messages = conversation.getMessages().stream()
                .map(this::toMessageDto)
                .collect(Collectors.toList());
        dto.setMessages(messages);

        return dto;
    }

    public MessageDTO getMessageById(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException("Message not found with ID: " + messageId));

        return toMessageDto(message);
    }

    public MessageDTO sendMessage(Long conversationId, MessageDTO messageDTO) {
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ConversationNotFoundException("Conversation not found with ID: " + conversationId));

        Utilisateur sender = new Utilisateur();
        sender.setId(messageDTO.getSenderId());

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(messageDTO.getContent());
        message.setTimestamp(LocalDateTime.now());

        return toMessageDto(messageRepository.save(message));
    }

    private MessageDTO toMessageDto(Message message) {
        MessageDTO dto = new MessageDTO();
        dto.setId(message.getId());
        dto.setSenderId(message.getSender().getId());
        dto.setContent(message.getContent());
        dto.setTimestamp(message.getTimestamp());
        return dto;
    }

    private ConversationDTO toDto(Conversation conversation) {
        ConversationDTO dto = new ConversationDTO();
        dto.setId(conversation.getId());
        dto.setUtilisateur1Id(conversation.getUtilisateur1().getId());
        dto.setUtilisateur2Id(conversation.getUtilisateur2().getId());
        dto.setAnnonceId(conversation.getAnnonce().getId());
        dto.setTitre(conversation.getTitre());
        return dto;
    }
}
