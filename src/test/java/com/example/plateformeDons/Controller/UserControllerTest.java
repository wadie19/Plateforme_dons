package com.example.plateformeDons.Controller;

import com.example.plateformeDons.Service.UtilisateurService;
import com.example.plateformeDons.models.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UtilisateurService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    private Utilisateur mockUser;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();

        // Setting up a mock Utilisateur for the tests
        mockUser = new Utilisateur();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setEmail("testuser@example.com");
    }

    @Test
    public void testCreateUser() throws Exception {
        // Simulating the user service returning a created user
        when(userService.createUser(any(Utilisateur.class))).thenReturn(mockUser);

        // Perform POST request to create a new user
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"email\":\"testuser@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"));

        // Verify that the service method was called
        verify(userService, times(1)).createUser(any(Utilisateur.class));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        // Simulate the user service returning a list of users
        when(userService.getAllUsers()).thenReturn(Collections.singletonList(mockUser));

        // Perform GET request to retrieve all users
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser"));

        // Verify that the service method was called
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testGetUserById_Success() throws Exception {
        // Simulate the user service returning a user by ID
        when(userService.getUserById(1L)).thenReturn(Optional.of(mockUser));

        // Perform GET request to retrieve user by ID
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));

        // Verify that the service method was called
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    public void testGetUserById_NotFound() throws Exception {
        // Simulate the user service returning empty Optional (user not found)
        when(userService.getUserById(999L)).thenReturn(Optional.empty());

        // Perform GET request to retrieve user by ID
        mockMvc.perform(get("/api/users/999"))
                .andExpect(status().isNotFound());

        // Verify that the service method was called
        verify(userService, times(1)).getUserById(999L);
    }

    @Test
    public void testGetUserByUsername() throws Exception {
        // Simulate the user service returning a user by username
        when(userService.getUserByUsername("testuser")).thenReturn(Optional.of(mockUser));

        // Perform GET request to retrieve user by username
        mockMvc.perform(get("/api/users/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));

        // Verify that the service method was called
        verify(userService, times(1)).getUserByUsername("testuser");
    }

    @Test
    public void testUpdateUser() throws Exception {
        // Create a mock user with the updated details
        Utilisateur updatedUser = new Utilisateur();
        updatedUser.setId(1L);
        updatedUser.setUsername("updateduser");
        updatedUser.setEmail("updateduser@example.com");

        // Mock the userService to return the updated user
        when(userService.updateUser(any(Utilisateur.class))).thenReturn(updatedUser);

        // Perform PUT request to update user details
        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"updateduser\",\"email\":\"updateduser@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updateduser"))
                .andExpect(jsonPath("$.email").value("updateduser@example.com"));

        // Verify that the service method was called
        verify(userService, times(1)).updateUser(any(Utilisateur.class));
    }

    @Test
    public void testDeleteUser() throws Exception {
        // Perform DELETE request to delete user
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        // Verify that the service method was called
        verify(userService, times(1)).deleteUser(1L);
    }
}
