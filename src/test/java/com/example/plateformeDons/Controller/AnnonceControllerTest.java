package com.example.plateformeDons.Controller;

import com.example.plateformeDons.DTO.AnnonceDTO;
import com.example.plateformeDons.Service.AnnonceService;
import com.example.plateformeDons.Service.UtilisateurService;
import com.example.plateformeDons.models.Annonce;
import com.example.plateformeDons.models.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AnnonceControllerTest {

    @Mock
    private AnnonceService annonceService;

    @Mock
    private UtilisateurService utilisateurService;

    @InjectMocks
    private AnnonceController annonceController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(annonceController).build();
    }

    @Test
    public void testGetAllAnnonces() throws Exception {
        Annonce annonce1 = new Annonce();
        Annonce annonce2 = new Annonce();
        when(annonceService.getAllAnnonces()).thenReturn(Arrays.asList(annonce1, annonce2));

        mockMvc.perform(get("/api/annonces"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(annonceService, times(1)).getAllAnnonces();
    }

    @Test
    public void testGetAnnonceById_NotFound() throws Exception {
        when(annonceService.getAnnonceById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/annonces/1"))
                .andExpect(status().isNotFound());

        verify(annonceService, times(1)).getAnnonceById(1L);
    }

    @Test
    public void testGetAnnonceById_Found() throws Exception {
        Annonce annonce = new Annonce();
        annonce.setTitle("Found Title");
        when(annonceService.getAnnonceById(1L)).thenReturn(Optional.of(annonce));

        mockMvc.perform(get("/api/annonces/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Found Title"));

        verify(annonceService, times(1)).getAnnonceById(1L);
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testUpdateAnnonce() throws Exception {
        Annonce existingAnnonce = new Annonce();
        existingAnnonce.setId(1L);
        existingAnnonce.setTitle("Old Title");

        Annonce updatedAnnonce = new Annonce();
        updatedAnnonce.setTitle("Updated Title");

        // Use any() for flexible argument matching
        when(annonceService.updateAnnonce(eq(1L), any(Annonce.class))).thenReturn(updatedAnnonce);

        mockMvc.perform(put("/api/annonces/1")
                        .contentType("application/json")
                        .content("{\"title\": \"Updated Title\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));

        verify(annonceService, times(1)).updateAnnonce(eq(1L), any(Annonce.class));
    }


    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testDeleteAnnonce() throws Exception {
        doNothing().when(annonceService).deleteAnnonce(1L);

        mockMvc.perform(delete("/api/annonces/1"))
                .andExpect(status().isNoContent());

        verify(annonceService, times(1)).deleteAnnonce(1L);
    }

    // Additional tests for search and other endpoints can be added in a similar manner.
}
