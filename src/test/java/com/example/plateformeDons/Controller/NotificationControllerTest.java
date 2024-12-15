package com.example.plateformeDons.Controller;

import org.springframework.http.MediaType;
import com.example.plateformeDons.DTO.NotificationDTO;
import com.example.plateformeDons.Service.NotificationService;
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
public class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(notificationController).build();
    }

    @Test
    public void testGetNotificationsByUtilisateur() throws Exception {
        // Create a list of NotificationDTO for the response
        NotificationDTO notificationDTO1 = new NotificationDTO();
        notificationDTO1.setId(1L);
        notificationDTO1.setMessage("Notification 1");
        notificationDTO1.setUtilisateurId(1L);
        notificationDTO1.setRead(false);

        NotificationDTO notificationDTO2 = new NotificationDTO();
        notificationDTO2.setId(2L);
        notificationDTO2.setMessage("Notification 2");
        notificationDTO2.setUtilisateurId(1L);
        notificationDTO2.setRead(false);

        List<NotificationDTO> notifications = Arrays.asList(notificationDTO1, notificationDTO2);

        // Simulate service call
        when(notificationService.getNotificationsByUtilisateur(1L)).thenReturn(notifications);

        // Perform GET request to fetch notifications by user ID
        mockMvc.perform(get("/api/notifications/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[0].message").value("Notification 1"))
                .andExpect(jsonPath("$[1].message").value("Notification 2"));

        verify(notificationService, times(1)).getNotificationsByUtilisateur(1L);
    }

    @Test
    public void testGetAllNotifications() throws Exception {
        // Create a list of NotificationDTO for the response
        NotificationDTO notificationDTO1 = new NotificationDTO();
        notificationDTO1.setId(1L);
        notificationDTO1.setMessage("Global Notification 1");
        notificationDTO1.setUtilisateurId(1L);
        notificationDTO1.setRead(false);

        NotificationDTO notificationDTO2 = new NotificationDTO();
        notificationDTO2.setId(2L);
        notificationDTO2.setMessage("Global Notification 2");
        notificationDTO2.setUtilisateurId(2L);
        notificationDTO2.setRead(false);

        List<NotificationDTO> notifications = Arrays.asList(notificationDTO1, notificationDTO2);

        // Simulate service call
        when(notificationService.getAllNotifs()).thenReturn(notifications);

        // Perform GET request to fetch all notifications
        mockMvc.perform(get("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[0].message").value("Global Notification 1"))
                .andExpect(jsonPath("$[1].message").value("Global Notification 2"));

        verify(notificationService, times(1)).getAllNotifs();
    }

    @Test
    public void testMarkAsRead() throws Exception {
        // Simulate service call
        doNothing().when(notificationService).markAsRead(1L);

        // Perform PUT request to mark a notification as read
        mockMvc.perform(put("/api/notifications/read/{notificationId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(notificationService, times(1)).markAsRead(1L);
    }
}
