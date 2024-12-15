package com.example.plateformeDons.Controller.Web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Controller
public class LoginController {

     private String extractTokenFromResponse(String responseBody) {
        try {
            // Create an ObjectMapper instance to parse the JSON
            ObjectMapper objectMapper = new ObjectMapper();
            
            // Parse the response body to a JsonNode
            JsonNode root = objectMapper.readTree(responseBody);
            
            // Extract the JWT token from the "jwtToken" field
            return root.path("jwtToken").asText();
        } catch (Exception e) {
            throw new RuntimeException("Error extracting token from response", e);
        }
    }

    private final String loginUrl = "http://localhost:8080/auth/login"; // Adjust this to your API endpoint

    // Serve the login page
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("isLoggedIn", false); // Default to false for login page
        return "login"; 
    }

    // Handle login form submission
 @PostMapping("/login")
public String processLogin(
        @RequestParam("username") String username,
        @RequestParam("password") String password,
        HttpSession session,  // Inject HttpSession to set session attributes
        Model model,
        HttpServletResponse response) {  // Inject HttpServletResponse to set cookies

    String requestBody = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Content-Type", "application/json");
    HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

    try {
        ResponseEntity<String> responseEntity = restTemplate.exchange(loginUrl, HttpMethod.POST, entity, String.class);

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            // Parse the JWT token from the response body (assuming JSON response)
            String responseBody = responseEntity.getBody();
            String jwtToken = extractTokenFromResponse(responseBody);

            // Set JWT token in a cookie (cookie expires in 1 hour)
            Cookie cookie = new Cookie("jwtToken", jwtToken);
            cookie.setHttpOnly(true); // Ensures the cookie is not accessible via JavaScript
            cookie.setSecure(true);  // Only send cookie over HTTPS (set to false for development)
            cookie.setMaxAge(60 * 60); // 1 hour expiration
            cookie.setPath("/");  // Set path to be available across the application
            response.addCookie(cookie);

            return "redirect:/"; // Redirect to home page
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login"; // Return to login page with error
        }
    } catch (Exception e) {
        model.addAttribute("error", "An error occurred while processing your login. Please try again.");
        return "login"; // Return to login page with error
    }

    

   
}
@GetMapping("/logout-user")
public String logout(HttpServletRequest request, HttpServletResponse response) {
  // Invalidate the JWT token (remove the cookie)
 
       Cookie cookie = new Cookie("jwtToken", "jwtToken");
            cookie.setHttpOnly(true);  
            cookie.setSecure(true);   
            cookie.setMaxAge(0);  
            cookie.setPath("/");  
            response.addCookie(cookie);

  // Clear HttpSession if needed (optional)
  HttpSession session = request.getSession(false);
  if (session != null) {
    session.invalidate();
  }

  return "redirect:/"; 
}

}