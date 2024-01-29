package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.entity.Restaurant;
import com.dostavljaci.FoodDelivery.service.RestaurantService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@AllArgsConstructor
@Controller
public class HomeController {
    private final RestaurantService restaurantService;
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        boolean isLoggedIn = session.getAttribute("user") != null;
        model.addAttribute("loggedIn", isLoggedIn);

        if (session.getAttribute("user") != null) {
            model.addAttribute("user", session.getAttribute("user"));
        }
        else{
            model.addAttribute("user", null);
        }
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        model.addAttribute("restaurants", restaurants);
        return "home"; // This should match the name of your Thymeleaf template
    }


}
