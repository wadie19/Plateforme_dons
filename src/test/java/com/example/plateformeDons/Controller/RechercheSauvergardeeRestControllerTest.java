package com.example.plateformeDons.Controller;

import com.example.plateformeDons.DTO.RechercheDTO;
import com.example.plateformeDons.Service.RechercheSauvegardeeService;
import com.example.plateformeDons.Service.UtilisateurService;
import com.example.plateformeDons.models.Recherche;
import com.example.plateformeDons.models.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class RechercheSauvergardeeRestControllerTest {

    @Mock
    private RechercheSauvegardeeService rechercheSauvegardeeService;

    @Mock
    private UtilisateurService utilisateurService;

    @InjectMocks
    private RechercheSauvergardeeRestController rechercheSauvergardeeRestController;

    private MockMvc mockMvc;

    private Utilisateur mockUtilisateur;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(rechercheSauvergardeeRestController).build();

        // Setting up a mock Utilisateur for the tests
        mockUtilisateur = new Utilisateur();
        mockUtilisateur.setUsername("testuser");
        mockUtilisateur.setId(1L);

        // Mocking authentication context for the logged-in user
        UserDetails mockUserDetails = org.mockito.Mockito.mock(UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("testuser");
        SecurityContextHolder.getContext().setAuthentication(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(mockUserDetails, null));
    }

    @Test
    public void testSauvegarderRecherche_Success() throws Exception {
        // Setting up the request parameters
        String zone = "Paris";
        String etat = "Disponible";
        String motCle = "Vélo";

        // Simulate the service call
        when(utilisateurService.getUserByUsername("testuser")).thenReturn(Optional.of(mockUtilisateur));
        doNothing().when(rechercheSauvegardeeService).sauvegarderRecherche(mockUtilisateur, zone, etat, motCle);

        // Perform POST request to save search
        mockMvc.perform(post("/api/recherches/sauvegarder")
                        .param("zone", zone)
                        .param("etat", etat)
                        .param("motCle", motCle))
                .andExpect(status().isCreated())
                .andExpect(content().string("Recherche sauvegardée avec succès"));

        // Verify that the service method was called
        verify(rechercheSauvegardeeService, times(1)).sauvegarderRecherche(mockUtilisateur, zone, etat, motCle);
    }

    @Test
    public void testSauvegarderRecherche_Failure() throws Exception {
        // Setting up the request parameters
        String zone = "Paris";
        String etat = "Disponible";
        String motCle = "Vélo";

        // Simulate the service call failure
        when(utilisateurService.getUserByUsername("testuser")).thenReturn(Optional.of(mockUtilisateur));
        doThrow(new IllegalArgumentException("Erreur lors de la sauvegarde de la recherche")).when(rechercheSauvegardeeService)
                .sauvegarderRecherche(mockUtilisateur, zone, etat, motCle);

        // Perform POST request to save search
        mockMvc.perform(post("/api/recherches/sauvegarder")
                        .param("zone", zone)
                        .param("etat", etat)
                        .param("motCle", motCle))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Erreur lors de la sauvegarde de la recherche: Erreur lors de la sauvegarde de la recherche"));

        // Verify that the service method was called
        verify(rechercheSauvegardeeService, times(1)).sauvegarderRecherche(mockUtilisateur, zone, etat, motCle);
    }

    @Test
    public void testGetSavedSearches_Success() throws Exception {
        // Creating a mock list of Recherche
        Recherche savedSearch1 = new Recherche();
        savedSearch1.setZone("Paris");
        savedSearch1.setEtat("Disponible");
        savedSearch1.setMotCle("Vélo");

        Recherche savedSearch2 = new Recherche();
        savedSearch2.setZone("Lyon");
        savedSearch2.setEtat("Réservé");
        savedSearch2.setMotCle("Voiture");

        // Simulating service call
        when(utilisateurService.getUserByUsername("testuser")).thenReturn(Optional.of(mockUtilisateur));
        when(rechercheSauvegardeeService.getRecherchesNonNotifiees(mockUtilisateur.getId())).thenReturn(Arrays.asList(savedSearch1, savedSearch2));

        // Perform GET request to retrieve saved searches
        mockMvc.perform(get("/api/recherches/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].zone").value("Paris"))
                .andExpect(jsonPath("$[1].zone").value("Lyon"));

        // Verify that the service method was called
        verify(rechercheSauvegardeeService, times(1)).getRecherchesNonNotifiees(mockUtilisateur.getId());
    }

    @Test
    public void testGetSavedSearches_Empty() throws Exception {
        // Simulating service call returning an empty list
        when(utilisateurService.getUserByUsername("testuser")).thenReturn(Optional.of(mockUtilisateur));
        when(rechercheSauvegardeeService.getRecherchesNonNotifiees(mockUtilisateur.getId())).thenReturn(List.of());

        // Perform GET request to retrieve saved searches
        mockMvc.perform(get("/api/recherches/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        // Verify that the service method was called
        verify(rechercheSauvegardeeService, times(1)).getRecherchesNonNotifiees(mockUtilisateur.getId());
    }
}
