package com.example.plateformeDons.Controller;

import com.example.plateformeDons.DTO.ConversationDTO;
import com.example.plateformeDons.DTO.MessageDTO;
import com.example.plateformeDons.Service.ConversationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ConversationControllerTest {

    @Mock
    private ConversationService conversationService;

    @InjectMocks
    private ConversationController conversationController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(conversationController).build();
    }

    @Test
    public void testCreateConversation() throws Exception {
        // Setup mock conversation DTO
        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setId(1L);
        conversationDTO.setTitre("Test Conversation"); // Use the correct field 'titre'

        when(conversationService.createConversation(any(ConversationDTO.class)))
                .thenReturn(conversationDTO);

        mockMvc.perform(post("/api/conversations/create")
                        .contentType("application/json")
                        .content("{\"titre\": \"Test Conversation\", \"utilisateur1Id\": 1, \"utilisateur2Id\": 2, \"annonceId\": 3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("Test Conversation"));

        verify(conversationService, times(1)).createConversation(any(ConversationDTO.class));
    }

    @Test
    public void testGetConversationsForUser() throws Exception {
        // Setup mock conversation list
        ConversationDTO conversation1 = new ConversationDTO();
        conversation1.setId(1L);
        conversation1.setTitre("Conversation 1");
        ConversationDTO conversation2 = new ConversationDTO();
        conversation2.setId(2L);
        conversation2.setTitre("Conversation 2");

        List<ConversationDTO> conversations = Arrays.asList(conversation1, conversation2);

        when(conversationService.getConversationsForUser(1L)).thenReturn(conversations);

        mockMvc.perform(get("/api/conversations/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titre").value("Conversation 1"))
                .andExpect(jsonPath("$[1].titre").value("Conversation 2"));

        verify(conversationService, times(1)).getConversationsForUser(1L);
    }

    @Test
    public void testGetConversationById() throws Exception {
        // Setup mock conversation DTO
        ConversationDTO conversationDTO = new ConversationDTO();
        conversationDTO.setId(1L);
        conversationDTO.setTitre("Test Conversation");

        when(conversationService.getConversationById(1L)).thenReturn(conversationDTO);

        mockMvc.perform(get("/api/conversations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titre").value("Test Conversation"));

        verify(conversationService, times(1)).getConversationById(1L);
    }

    @Test
    public void testSendMessage() throws Exception {
        // Setup mock message DTO
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId(1L);
        messageDTO.setContent("Test message");

        when(conversationService.sendMessage(eq(1L), any(MessageDTO.class)))
                .thenReturn(messageDTO);

        mockMvc.perform(post("/api/conversations/1/messages")
                        .contentType("application/json")
                        .content("{\"content\": \"Test message\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test message"));

        verify(conversationService, times(1)).sendMessage(eq(1L), any(MessageDTO.class));
    }
}
