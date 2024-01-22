package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.entity.Address;
import com.dostavljaci.FoodDelivery.entity.Restaurant;
import com.dostavljaci.FoodDelivery.entity.User;
import com.dostavljaci.FoodDelivery.service.AddressService;
import com.dostavljaci.FoodDelivery.service.GeocodeService;
import com.dostavljaci.FoodDelivery.service.RestaurantService;
import com.dostavljaci.FoodDelivery.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        return "restaurant-form";
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
            Model model,
            HttpSession session) {

        Object sessionUser = session.getAttribute("user");
        User user;

        if (sessionUser instanceof User userInstance){
            user = userService.getUserByUsername(userInstance.getUsername());
        }
        else {
            throw new RuntimeException("Cant locate user:" + sessionUser);
        }

        //save the address and restaurant
        Address address = addressService.getAddressByCityStreetCountryPostalCode(city,street,country,postalCode);
        if (address == null){
            address = addressService.saveAddress(city,street,province,country,postalCode);
        }
        Restaurant savedRestaurant = restaurantService.saveRestaurant(restaurantName,contactNumber,user,(float)0,address);


        model.addAttribute(addressService.getAddressById(address.getId()));
        model.addAttribute(restaurantService.getRestaurantById(savedRestaurant.getId()));

        return "home";
    }

}
