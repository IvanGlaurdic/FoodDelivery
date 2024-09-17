package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.entity.Address;
import com.dostavljaci.FoodDelivery.entity.Restaurant;
import com.dostavljaci.FoodDelivery.entity.User;
import com.dostavljaci.FoodDelivery.service.MenuItemService;
import com.dostavljaci.FoodDelivery.service.RestaurantService;
import com.dostavljaci.FoodDelivery.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Controller
public class HomeController {
    private final RestaurantService restaurantService;
    private final UserService userService;
    private final MenuItemService menuItemService;
    @GetMapping("/")
    public String home(Model model, HttpSession session) {

        boolean isLoggedIn = session.getAttribute("user") != null;
        model.addAttribute("isLoggedIn", isLoggedIn);

        if (session.getAttribute("user") != null) {
            Object sessionUser = session.getAttribute("user");
            if (sessionUser instanceof User userInstance) {
                User user = userService.getUserByUsername(userInstance.getUsername());
                Map<Restaurant, Address> closestAddresses = restaurantService.findClosestAddressesForAllRestaurants(user.getAddress());
                session.setAttribute("user", user);
                model.addAttribute("closestAddresses", closestAddresses);
                model.addAttribute("user", user);
            } else {
                model.addAttribute("user", null);
            }
        }

        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        model.addAttribute("restaurants", restaurants);

        List<String> categories=menuItemService.getAllUniqueCategories();
        model.addAttribute("categories",categories);

        return "home";
    }

    @PostMapping("/search")
    public String homeSearch(@RequestParam String query, HttpSession session, Model model){
        // Convert the input query to lowercase
        String lowercaseQuery = query.toLowerCase();

        // Retrieve the restaurant by the case-insensitive name search
        Restaurant restaurant = restaurantService.getRestaurantByNameIgnoreCase(lowercaseQuery);

        if (restaurant == null) {
            return "redirect:/";
        }
        boolean isLoggedIn = session.getAttribute("user") != null;
        model.addAttribute("isLoggedIn", isLoggedIn);

        return "redirect:/order/" + restaurant.getName();

    }


    @GetMapping("/category/{category}")
    public String homeSearchCategory(@PathVariable String category,
                                     Model model,
                                     HttpSession session){

        boolean isLoggedIn = session.getAttribute("user") != null;
        model.addAttribute("isLoggedIn", isLoggedIn);

        if(session.getAttribute("user") instanceof  User user){
            model.addAttribute("user", user);
        }


        List<Restaurant> restaurants=restaurantService.getRestaurantsByMenuItemCategory(category);
        model.addAttribute("restaurants",restaurants);
        return "category-page";

    }

}
