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

        User user = userService.getUserByUsername("bg121788");
        Address savedAddress = addressService.saveAddress(city,street,province,country,postalCode);
        Restaurant savedRestaurant = restaurantService.saveRestaurant(restaurantName,contactNumber,user,(float)0);

        addressService.updateAddressRestaurant(savedAddress, savedRestaurant);

        model.addAttribute(addressService.getAddressById(savedAddress.getId()));
        model.addAttribute(restaurantService.getRestaurantById(savedRestaurant.getId()));

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
