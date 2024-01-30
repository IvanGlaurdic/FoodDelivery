package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.entity.Address;
import com.dostavljaci.FoodDelivery.entity.Restaurant;
import com.dostavljaci.FoodDelivery.entity.User;
import com.dostavljaci.FoodDelivery.service.RestaurantService;
import com.dostavljaci.FoodDelivery.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Controller
public class HomeController {
    private final RestaurantService restaurantService;
    private final UserService userService;
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        boolean isLoggedIn = session.getAttribute("user") != null;
        model.addAttribute("loggedIn", isLoggedIn);

        if (session.getAttribute("user") != null) {
            Object sessionUser = session.getAttribute("user");
            if (sessionUser instanceof User userInstance) {
                User user = userService.getUserByUsername(userInstance.getUsername());
                Map<Restaurant, Address> closestAddresses = restaurantService.findClosestAddressesForAllRestaurants(user.getAddress());
                model.addAttribute("closestAddresses", closestAddresses);
                model.addAttribute("user", user);
            } else {
                model.addAttribute("user", null);
            }
        }
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        model.addAttribute("restaurants", restaurants);



        return "home"; // This should match the name of your Thymeleaf template
    }
}
