package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.service.AddressService;
import com.dostavljaci.FoodDelivery.service.RestaurantService;
import com.dostavljaci.FoodDelivery.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping("/address")
@Controller
@AllArgsConstructor
public class AddressController {
    private final AddressService addressService;
    private final RestaurantService restaurantService;


    @PostMapping("/delete-address/{addressId}/{restaurantId}")
    public String removeAddressFromRestaurant(
            @PathVariable UUID addressId,
            @PathVariable UUID restaurantId) {


        addressService.removeAddressFromRestaurant(addressId, restaurantId);

        return "redirect:/restaurant/edit-restaurant/" +  restaurantService.getRestaurantById(restaurantId).getName();
    }

}
