package com.example.plateformeDons.Controller.Web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class RegisterController {

    private final String REGISTER_API_URL = "http://localhost:8080/auth/register";

    @GetMapping("/register")
    public String showRegistrationPage() {
        return "register"; // Returns the register.html Thymeleaf template
    }

    @PostMapping("/register")
    public String processRegistration(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("confirm-password") String confirmPassword,
            @RequestParam("email") String email,
            Model model) {

        // Check if fields are empty
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || confirmPassword.isEmpty()) {
            model.addAttribute("error", "All fields are required.");
            return "register";
        }

        // Check if passwords match
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "register";
        }

        // Create payload for the API
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("username", username);
        requestBody.put("email", email);
        requestBody.put("password", password);

try {
    // Use RestTemplate to send a POST request to the API
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.postForObject(REGISTER_API_URL, requestBody, String.class);

    // If successful, redirect to login
    model.addAttribute("message", "Registration successful. You can now log in.");
    return "login";
} catch (Exception e) {
    // Log the exception for debugging
    e.printStackTrace();
    model.addAttribute("error", "An error occurred while processing the registration. Please try again.");
    return "register";
}
    }
}
