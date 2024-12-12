package com.example.plateformeDons.Controller;

import com.example.plateformeDons.DTO.ConversationDTO;
import com.example.plateformeDons.DTO.MessageDTO;
import com.example.plateformeDons.Service.ConversationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/conversations")
public class ConversationController {

    @Autowired
    private ConversationService conversationService;

    @PostMapping("/create")
    public ResponseEntity<ConversationDTO> createConversation(@RequestBody ConversationDTO conversationDTO) {
        return ResponseEntity.ok(conversationService.createConversation(conversationDTO));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ConversationDTO>> getConversationsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(conversationService.getConversationsForUser(userId));
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<ConversationDTO> getConversationById(@PathVariable Long conversationId) {
        return ResponseEntity.ok(conversationService.getConversationById(conversationId));
    }

    @PostMapping("/{conversationId}/messages")
    public ResponseEntity<MessageDTO> sendMessage(@PathVariable Long conversationId, @RequestBody MessageDTO messageDTO) {
        return ResponseEntity.ok(conversationService.sendMessage(conversationId, messageDTO));
    }
}
