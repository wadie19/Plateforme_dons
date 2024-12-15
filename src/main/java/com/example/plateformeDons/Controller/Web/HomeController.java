package com.example.plateformeDons.Controller.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.example.plateformeDons.models.Annonce;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Arrays;
import java.util.List;

@Controller
public class HomeController {

    private final RestTemplate restTemplate;

    @Autowired
    public HomeController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Display the home page with ads fetched from the API.
     *
     * @param model Model to pass data to the view.
     * @param session HttpSession to check for the JWT token.
     * @return The name of the Thymeleaf template for the home page.
     */
    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {
        // Fetch ads from the API
        List<Annonce> annonces = fetchAdsFromApi();

       // Check if the jwtToken cookie exists
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
        model.addAttribute("annonces", annonces);

        // Return the home page template
        return "home";
    }

    /**
     * Fetch ads from the API.
     *
     * @return List of ads.
     */
    private List<Annonce> fetchAdsFromApi() {
        String apiUrl = "http://localhost:8080/api/annonces"; // Adjust the URL if needed
        Annonce[] adsArray = restTemplate.getForObject(apiUrl, Annonce[].class);
        return Arrays.asList(adsArray);
    }
}
