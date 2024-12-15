package com.example.plateformeDons.Controller.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.example.plateformeDons.models.Notification;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@Controller
public class MesNotificationsController {

    private final RestTemplate restTemplate;

    @Autowired
    public MesNotificationsController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Display the notifications page.
     *
     * @param model Model to pass data to the view.
     * @param request HttpServletRequest to retrieve the cookies.
     * @return The name of the Thymeleaf template for the notifications page.
     */
    @GetMapping("/mes-notifications")
    public String mesNotifications(Model model, HttpServletRequest request) {
        
        boolean isLoggedIn = false;
        String userId = null;

        // Check if the user is logged in via cookies
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwtToken".equals(cookie.getName()) && cookie.getValue() != null) {
                    isLoggedIn = true;
                    // Assuming you have a way to extract the userId from the cookie (JWT or otherwise)
                    break;
                }
            }
        }

 
            try {
                // Fetch notifications from the API
                String url = "http://localhost:8080/api/notifications/";
                // Assuming the response is a list of notifications
                List<Notification> notifications = restTemplate.getForObject(url, List.class);
                model.addAttribute("notifications", notifications);
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("error", "Could not fetch notifications.");
            }
      

        // Add the login status to the model
        model.addAttribute("isLoggedIn", isLoggedIn);

        // Return the Thymeleaf template for the notifications page
        return "mes-notifications";
    }

}
