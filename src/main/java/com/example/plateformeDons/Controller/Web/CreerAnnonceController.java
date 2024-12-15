package com.example.plateformeDons.Controller.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
 import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CreerAnnonceController {

    private final RestTemplate restTemplate;

    private final String ANNONCES_API_URL = "http://localhost:8080/api/annonces";

    @Autowired
    public CreerAnnonceController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/creer-une-annonce")
    public String showCreateAnnonceForm(Model model,  HttpServletRequest request) {
    
    boolean isLoggedIn = false;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if ("jwtToken".equals(cookie.getName()) && cookie.getValue() != null) {
                isLoggedIn = true;
                break;
            }
        }
    }
        model.addAttribute("isLoggedIn", isLoggedIn); 

        return "creer-une-annonce"; // Returns the Thymeleaf template for the form
    }
@PostMapping("/creer-une-annonce")
public String createAnnonce(
        @RequestParam("title") String title,
        @RequestParam("description") String description,
        @RequestParam("etat") String etat,
        @RequestParam("zoneGeographique") String zoneGeographique,
        @RequestParam("modaliteDon") String modaliteDon,
        @RequestParam("motCles") String motCles,
        Model model,
        HttpServletRequest request) {

    // Validate the form fields
    if (title.isEmpty() || description.isEmpty() || etat.isEmpty() || zoneGeographique.isEmpty() || modaliteDon.isEmpty()) {
        model.addAttribute("error", "All fields are required.");
        return "creer-une-annonce";
    }

    // Retrieve the token from cookies
    String jwtToken = null;
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
        for (Cookie cookie : cookies) {
            if ("jwtToken".equals(cookie.getName())) {
                jwtToken = cookie.getValue();
                break;
            }
        }
    }

    if (jwtToken == null) {
        model.addAttribute("error", "Authentication required to create an annonce.");
        return "creer-une-annonce";
    }

    // Prepare the request body
    Map<String, Object> requestBody = new HashMap<>();
    requestBody.put("title", title);
    requestBody.put("description", description);
    requestBody.put("etat", etat);
    requestBody.put("datePublication", LocalDate.now().toString());
    requestBody.put("zoneGeographique", zoneGeographique);
    requestBody.put("modaliteDon", modaliteDon);
    requestBody.put("motCles", motCles.split(",")); // Split keywords by commas

    try {
        // Set the Authorization header with Bearer token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtToken); // Set the Bearer token

        // Prepare the HTTP request entity with headers and body
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send the POST request to the API
        restTemplate.exchange(ANNONCES_API_URL, HttpMethod.POST, requestEntity, String.class);

        model.addAttribute("message", "Annonce created successfully!");
        return "redirect:/"; // Redirect to the home page after successful creation
    } catch (Exception e) {
        e.printStackTrace();
        model.addAttribute("error", "An error occurred while creating the annonce.");
        return "creer-une-annonce";
    }
}
}
