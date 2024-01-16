package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.entity.Restaurant;
import com.dostavljaci.FoodDelivery.entity.User;
import com.dostavljaci.FoodDelivery.service.RestaurantService;
import com.dostavljaci.FoodDelivery.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.http.HttpRequest;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@AllArgsConstructor
@Controller
public class UserController {
    private final UserService userService;
    private final RestaurantService restaurantService;

    @GetMapping("/profile")
    public String profile(Model model, String username) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            List<User> users = null;
            List<Restaurant> restaurants = null;
            if (Objects.equals(user.getRole().toLowerCase(), "admin")){
                users = userService.getAllUsers();
                restaurants = restaurantService.getAllRestaurants();
            } else if (Objects.equals(user.getRole().toLowerCase(), "owner")) {
                restaurants = restaurantService.getRestaurantsByOwner(user.getId());
            }
            model.addAttribute("user", user);
            model.addAttribute("users", users);
            model.addAttribute("restaurants", restaurants);
            return "profile";
        } else {
            // User is not authenticated, redirect to login page
            return "redirect:/login";
        }
    }

}
