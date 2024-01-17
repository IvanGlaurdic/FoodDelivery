package com.dostavljaci.FoodDelivery.service;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

;
@Service
@AllArgsConstructor
public class GeocodeService {
    private final RestTemplate restTemplate;


    public Map<String, Double> geocodeAddress(String address) {
        Map<String, Double> coordinates = new HashMap<>();
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8).replace("+", "%20");
            String graphhopperLink = "https://graphhopper.com/api/1/geocode?";
            String graphhopperApiKey = "24f245a4-bd48-4a72-b873-3710a6d8bab0";
            String url = String.format(graphhopperLink + "q=%s&limit=1&key=%s", encodedAddress, graphhopperApiKey);

            ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

            System.out.println("Response from GraphHopper API: " + response.getBody().toString());


            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode hits = response.getBody().path("hits");
                if (!hits.isEmpty()) {
                    JsonNode firstHit = hits.get(0);
                    JsonNode point = firstHit.path("point");
                    double lat = point.path("lat").asDouble();
                    double lng = point.path("lng").asDouble();

                    coordinates.put("latitude", lat);
                    coordinates.put("longitude", lng);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return coordinates;
    }


}
