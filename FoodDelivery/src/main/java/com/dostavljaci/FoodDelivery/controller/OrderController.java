package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dostavljaci.FoodDelivery.service.RestaurantService;
import com.dostavljaci.FoodDelivery.entity.Restaurant;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/order")
public class OrderController {
    public final OrderService orderService;
    private final RestaurantService restaurantService;

    @GetMapping("/{Name}")
    public String orderPage(@PathVariable String Name, Model model) {
        Restaurant restaurant=restaurantService.getRestaurantByName(Name);
        model.addAttribute("restaurant", restaurant);
        return "order-page";
    }
}
