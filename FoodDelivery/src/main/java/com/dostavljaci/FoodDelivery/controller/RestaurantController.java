package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.entity.Address;
import com.dostavljaci.FoodDelivery.entity.Restaurant;
import com.dostavljaci.FoodDelivery.entity.User;
import com.dostavljaci.FoodDelivery.service.AddressService;
import com.dostavljaci.FoodDelivery.service.GeocodeService;
import com.dostavljaci.FoodDelivery.service.RestaurantService;
import io.redlink.geocoding.LatLon;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class RestaurantController {
    public final RestaurantService restaurantService;
    public final AddressService addressService;
    public final GeocodeService geocodeService;
    @GetMapping("/restaurant")
    public String showRestaurantForm(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        model.addAttribute("address", new Address());
        return "RestaurantForm";
    }

    @PostMapping("/restaurant")
    public String handleRestaurantSubmission(@ModelAttribute Restaurant restaurant, @ModelAttribute Address address, Model model) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        restaurant.setOwner(user);
        Restaurant savedRestaurant = restaurantService.saveRestaurant(restaurant);

        LatLon geocodedLatLon = geocodeService.geocodeAddress(address.toString());
        address.setLongitude((float) geocodedLatLon.lon());
        address.setLatitude((float) geocodedLatLon.lat());
        address.setRestaurant(savedRestaurant);

        addressService.saveAddress(address);
        return "home"; // Assuming you have a Thymeleaf template for address form
    }

    @PostMapping("/restaurant/{restaurantId}/address")
    public String handleAddressSubmission(@PathVariable UUID restaurantId, @ModelAttribute Address address) throws IOException {
        // Retrieve the restaurant entity by ID
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        // Set the retrieved restaurant to the address
        address.setRestaurant(restaurant);

        // Save the address
        addressService.saveAddress(address);

        return "home";
    }
}
