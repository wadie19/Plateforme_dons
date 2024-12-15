package com.example.plateformeDons.Controller.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import com.example.plateformeDons.models.Annonce;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AnnonceDetailsController {  // Renamed class

    private final RestTemplate restTemplate;

    @Autowired
    public AnnonceDetailsController(RestTemplate restTemplate) {  // Adjust constructor name
        this.restTemplate = restTemplate;
    }

    /**
     * Display the details of a specific annonce.
     *
     * @param id The ID of the annonce to fetch.
     * @param model Model to pass data to the view.
     * @return The name of the Thymeleaf template for the annonce details page.
     */
    @GetMapping("/annonces/{id}")
    public String annonceDetails(@PathVariable("id") Long id, Model model, HttpServletRequest request) {
        // Fetch the specific annonce from the API
        Annonce annonce = fetchAnnonceFromApi(id);
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
        // Add the annonce to the model
        model.addAttribute("annonce", annonce);

        // Return the annonce details page template
        return "annonce-details";
    }

    /**
     * Fetch a specific annonce from the API.
     *
     * @param id The ID of the annonce to fetch.
     * @return The annonce.
     */
    private Annonce fetchAnnonceFromApi(Long id) {
        String apiUrl = "http://localhost:8080/api/annonces/" + id;
    String response = restTemplate.getForObject(apiUrl, String.class);
    System.out.println("Response: " + response);  // Log the raw JSON response
    return restTemplate.getForObject(apiUrl, Annonce.class);
    }
}
