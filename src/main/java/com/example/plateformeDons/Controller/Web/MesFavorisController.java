package com.example.plateformeDons.Controller.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.example.plateformeDons.models.Annonce;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;

@Controller
public class MesFavorisController {

    private final RestTemplate restTemplate;

    @Autowired
    public MesFavorisController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Display the home page with ads fetched from the API.
     *
     * @param model Model to pass data to the view.
     * @return The name of the Thymeleaf template for the home page.
     */
    @GetMapping("/favoris")
    public String home(Model model, HttpServletRequest request) {
        // Fetch ads from the API
        List<Annonce> annonces = fetchAdsFromApi(request);

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
        // Add ads to the model
        model.addAttribute("annonces", annonces);

        // Return the annonces page template
        return "favoris";
    }

    /**
     * Fetch ads from the API with Bearer token in the header.
     *
     * @param request The HTTP request to get the JWT token.
     * @return List of ads.
     */
    private List<Annonce> fetchAdsFromApi(HttpServletRequest request) {
        String apiUrl = "http://localhost:8080/api/favoris/favoris";
        
        // Get the JWT token from cookies
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
        
        // Set the Bearer token in the request header
        HttpHeaders headers = new HttpHeaders();
        if (jwtToken != null) {
            headers.set("Authorization", "Bearer " + jwtToken);
        }

        // Create a request entity with the headers
        ResponseEntity<Annonce[]> response = restTemplate.exchange(
            apiUrl, HttpMethod.GET, new org.springframework.http.HttpEntity<>(headers), Annonce[].class);

        // Return the list of ads
        return Arrays.asList(response.getBody());
    }
}
