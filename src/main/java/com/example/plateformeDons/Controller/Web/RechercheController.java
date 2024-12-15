package com.example.plateformeDons.Controller.Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.example.plateformeDons.models.Annonce;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;

@Controller
public class RechercheController {

    private final RestTemplate restTemplate;

    @Autowired
    public RechercheController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/recherche")
public String recherche(
        @RequestParam(value = "motCle", required = false) String motCle,
        @RequestParam(value = "zone", required = false) String zone,
        @RequestParam(value = "etat", required = false) String etat,
        Model model,
        HttpServletRequest request) {

    // Fetch filtered ads from the API
    List<Annonce> annonces = fetchFilteredAdsFromApi(motCle, zone, etat);

    // Check login status
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
    model.addAttribute("motCle", motCle); // For displaying search keyword
    model.addAttribute("zone", zone);    // To pre-select the filter value
    model.addAttribute("etat", etat);    // To pre-select the filter value

    return "recherche";
}

/**
 * Fetch filtered ads from the API.
 *
 * @param motCle Keyword for filtering.
 * @param zone Zone for filtering.
 * @param etat State (condition) for filtering.
 * @return List of filtered ads.
 */
private List<Annonce> fetchFilteredAdsFromApi(String motCle, String zone, String etat) {
    String apiUrl = "http://localhost:8080/api/annonces/recherche?";
    StringBuilder urlBuilder = new StringBuilder(apiUrl);

    // Append query parameters if present
    if (motCle != null && !motCle.isEmpty()) {
        urlBuilder.append("motCle=").append(motCle).append("&");
    }
    if (zone != null && !zone.isEmpty()) {
        urlBuilder.append("zone=").append(zone).append("&");
    }
    if (etat != null && !etat.isEmpty()) {
        urlBuilder.append("etat=").append(etat).append("&");
    }

    // Remove trailing "&" or "?" if no params
    String finalUrl = urlBuilder.toString().replaceAll("[&?]$", "");

    // Fetch data from the API
    Annonce[] adsArray = restTemplate.getForObject(finalUrl, Annonce[].class);
    return Arrays.asList(adsArray);
}
}
