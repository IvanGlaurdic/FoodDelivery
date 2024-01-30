package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.entity.User;
import com.dostavljaci.FoodDelivery.service.AddressService;
import com.dostavljaci.FoodDelivery.service.RestaurantService;
import com.dostavljaci.FoodDelivery.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@RequestMapping("/address")
@Controller
@AllArgsConstructor
public class AddressController {
    private final AddressService addressService;
    private final RestaurantService restaurantService;
    private final UserService userService;


    @PostMapping("/delete-address/{addressId}/{restaurantId}")
    public String removeAddressFromRestaurant(
            @PathVariable UUID addressId,
            @PathVariable UUID restaurantId) {


        addressService.removeAddressFromRestaurant(addressId, restaurantId);

        return "redirect:/restaurant/edit-restaurant/" +  restaurantService.getRestaurantById(restaurantId).getName();
    }

}
