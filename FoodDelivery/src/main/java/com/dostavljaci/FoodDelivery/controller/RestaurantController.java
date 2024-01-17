package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.entity.Address;
import com.dostavljaci.FoodDelivery.entity.Restaurant;
import com.dostavljaci.FoodDelivery.entity.User;
import com.dostavljaci.FoodDelivery.service.AddressService;
import com.dostavljaci.FoodDelivery.service.GeocodeService;
import com.dostavljaci.FoodDelivery.service.RestaurantService;
import com.dostavljaci.FoodDelivery.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class RestaurantController {
    public final RestaurantService restaurantService;
    public final AddressService addressService;
    public final GeocodeService geocodeService;
    public final UserService userService;
    @GetMapping("/restaurant")
    public String showRestaurantForm(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        model.addAttribute("address", new Address());
        model.addAttribute("error", null);
        return "RestaurantForm";
    }

    @PostMapping("/restaurant")
    public String handleRestaurantSubmission(
                                             @RequestParam String restaurantName,
                                             @RequestParam String contactNumber,
                                             @RequestParam String street,
                                             @RequestParam String city,
                                             @RequestParam String province,
                                             @RequestParam String country,
                                             @RequestParam String postalCode,
                                             Model model) {

        try {
            User user = userService.getUserByUsername("bg121788");
            Restaurant restaurant = new Restaurant();
            restaurant.setOwner(user);
            restaurant.setName(restaurantName);
            restaurant.setContactNumber(contactNumber);
            restaurant.setRating((float)0);
            Restaurant savedRestaurant = restaurantService.saveRestaurant(restaurant);

            Address address = new Address();
            address.setCity(city);
            address.setStreet(street);
            address.setProvince(province);
            address.setCountry(country);
            address.setPostalCode(postalCode);


            Map<String, Double> geocodedLatLon = geocodeService.geocodeAddress(address.toString());
            address.setLongitude(geocodedLatLon.get("lon").floatValue());
            address.setLatitude(geocodedLatLon.get("lat").floatValue());
            address.setRestaurant(savedRestaurant);

            addressService.saveAddress(address);

            model.addAttribute(restaurant);
            model.addAttribute(address);
        }

        catch (IOException exception){
            model.addAttribute("error",  exception.getMessage());
        }

        return "tester"; // Assuming you have a Thymeleaf template for address form
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
