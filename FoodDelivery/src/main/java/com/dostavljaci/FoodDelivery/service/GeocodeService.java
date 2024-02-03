package com.dostavljaci.FoodDelivery.service;


import com.dostavljaci.FoodDelivery.entity.Address;
import com.dostavljaci.FoodDelivery.entity.Restaurant;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@Service
@AllArgsConstructor
public class GeocodeService {
    private final RestTemplate restTemplate;
    private final String graphhopperApiKey= "05ce8031-8243-4b47-b1fb-534bf8d3f6b4";


    public Map<String, Object> calculateDistanceAndTime(
            String startAddressLatitude,
            String startAddressLongitude,
            String endAddressLatitude,
            String endAddressLongitude) {

        Map<String, Object> result = new HashMap<>();
        try {
            String url = String.format(
                    "https://graphhopper.com/api/1/route?point=%s,%s&point=%s,%s&vehicle=car&key=%s",
                    URLEncoder.encode(startAddressLatitude, StandardCharsets.UTF_8),
                    URLEncoder.encode(startAddressLongitude, StandardCharsets.UTF_8),
                    URLEncoder.encode(endAddressLatitude, StandardCharsets.UTF_8),
                    URLEncoder.encode(endAddressLongitude, StandardCharsets.UTF_8),
                    graphhopperApiKey
            );

            ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode path = response.getBody().path("paths").get(0);
                double distance = path.path("distance").asDouble(); // Distance in meters
                long time = path.path("time").asLong(); // Time in milliseconds

                result.put("distance", distance);
                result.put("time", time);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    public Map<String, Double> geocodeAddress(String address) {
        Map<String, Double> coordinates = new HashMap<>();
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8).replace("+", "%20");
            String graphhopperLink = "https://graphhopper.com/api/1/geocode?";
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


    public long getScheduledDeliveryTime(Restaurant restaurant, Address userAddress) {
        Address closestAddress = null;
        double minDistance = Double.MAX_VALUE;
        long estimatedDeliveryTime = 0;

        for (Address address : restaurant.getAddress()) {
            Map<String, Object> distanceAndTime = calculateDistanceAndTime(
                    userAddress.getLatitude().toString(),
                    userAddress.getLongitude().toString(),
                    address.getLatitude().toString(),
                    address.getLongitude().toString()
            );

            double distance = (double) distanceAndTime.get("distance");
            long time = (long) distanceAndTime.get("time");
            if (distance < minDistance) {
                minDistance = distance;
                closestAddress = address;
                estimatedDeliveryTime = time; // Time in milliseconds
            }
        }

        return estimatedDeliveryTime;
    }


}
