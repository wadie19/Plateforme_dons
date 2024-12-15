package com.example.plateformeDons.Controller;

import com.example.plateformeDons.DTO.ConversationDTO;
import com.example.plateformeDons.DTO.MessageDTO;
import com.example.plateformeDons.Service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    // Create a new conversation
    @PostMapping(consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<ConversationDTO> createConversation(@RequestBody ConversationDTO conversationDTO) {
        return ResponseEntity.ok(conversationService.createConversation(conversationDTO));
    }

    // Get all conversations for a user
    @GetMapping(value = "/user/{userId}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<List<ConversationDTO>> getConversationsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(conversationService.getConversationsForUser(userId));
    }

    // Get a conversation by its ID
    @GetMapping(value = "/{conversationId}", produces = MediaType.ALL_VALUE)
    public ResponseEntity<ConversationDTO> getConversationById(@PathVariable Long conversationId) {
        return ResponseEntity.ok(conversationService.getConversationById(conversationId));
    }

    // Send a message to a conversation
    @PostMapping(value = "/{conversationId}/messages", consumes = MediaType.ALL_VALUE, produces = MediaType.ALL_VALUE)
    public ResponseEntity<MessageDTO> sendMessage(@PathVariable Long conversationId, @RequestBody MessageDTO messageDTO) {
        return ResponseEntity.ok(conversationService.sendMessage(conversationId, messageDTO));
    }
}
