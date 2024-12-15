package com.example.plateformeDons.Controller;

import com.example.plateformeDons.DTO.AnnonceDTO;
import com.example.plateformeDons.Service.FavorisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FavorisControllerTest {

    @Mock
    private FavorisService favorisService;

    @InjectMocks
    private FavorisController favorisController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(favorisController).build();
    }

    @Test
    public void testAjouterAuxFavoris() throws Exception {
        // Set up mock Authentication with a dummy username
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long annonceId = 1L;

        // Simulate service call
        doNothing().when(favorisService).ajouterAuxFavoris("testUser", annonceId);

        // Perform POST request to add to favorites
        mockMvc.perform(post("/api/favoris/ajouter")
                        .param("annonceId", String.valueOf(annonceId)))
                .andExpect(status().isOk())
                .andExpect(content().string("Annonce ajoutée aux favoris"));

        verify(favorisService, times(1)).ajouterAuxFavoris("testUser", annonceId);
    }

    @Test
    public void testRetirerDesFavoris() throws Exception {
        // Set up mock Authentication with a dummy username
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Long annonceId = 1L;

        // Simulate service call
        doNothing().when(favorisService).retirerDesFavoris("testUser", annonceId);

        // Perform DELETE request to remove from favorites
        mockMvc.perform(delete("/api/favoris/retirer")
                        .param("annonceId", String.valueOf(annonceId)))
                .andExpect(status().isOk())
                .andExpect(content().string("Annonce retirée des favoris"));

        verify(favorisService, times(1)).retirerDesFavoris("testUser", annonceId);
    }

    @Test
    public void testGetFavoris() throws Exception {
        // Set up mock Authentication with a dummy username
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Create a list of AnnonceDTO for the response
        AnnonceDTO annonceDTO1 = new AnnonceDTO();
        annonceDTO1.setTitle("Test Title 1");
        AnnonceDTO annonceDTO2 = new AnnonceDTO();
        annonceDTO2.setTitle("Test Title 2");

        List<AnnonceDTO> favorisList = Arrays.asList(annonceDTO1, annonceDTO2);

        // Simulate service call
        when(favorisService.getFavorisByUsername("testUser")).thenReturn(favorisList);

        // Perform GET request to retrieve favorites
        mockMvc.perform(get("/api/favoris/favoris"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Title 1"))
                .andExpect(jsonPath("$[1].title").value("Test Title 2"));

        verify(favorisService, times(1)).getFavorisByUsername("testUser");
    }
}
