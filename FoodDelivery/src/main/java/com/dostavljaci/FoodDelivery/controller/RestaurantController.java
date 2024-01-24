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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@AllArgsConstructor
@RequestMapping("/restaurant")
public class RestaurantController {
    public final RestaurantService restaurantService;
    public final AddressService addressService;
    public final GeocodeService geocodeService;
    public final UserService userService;
    @GetMapping()
    public String showRestaurantForm(Model model) {
        model.addAttribute("restaurant", new Restaurant());
        model.addAttribute("address", new Address());
        model.addAttribute("error", null);
        return "add-restaurant";
    }

    @PostMapping()
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
        Address address = addressService.getAddressByAllParams(city,street,province,country,postalCode);
        if (address == null){
            address = addressService.saveAddress(city,street,province,country,postalCode);
        }
        Restaurant savedRestaurant = restaurantService.saveRestaurant(restaurantName,contactNumber,user,(float)0,address);


        model.addAttribute(addressService.getAddressById(address.getId()));
        model.addAttribute(restaurantService.getRestaurantById(savedRestaurant.getId()));

        return "redirect:/profile/" + user.getUsername();
    }

    @PostMapping("/delete/{id}")
    public String deleteRestaurant(@PathVariable UUID id, RedirectAttributes redirectAttributes, HttpSession httpSession, Model model) {
        User user;
        Object sessionUser = httpSession.getAttribute("user");
        System.out.print(sessionUser);
        if(sessionUser instanceof User userInstance){
            user = userService.getUserByUsername(userInstance.getUsername());
        }else {
            model.addAttribute("error", "User authentication failed.");
            return "redirect:/login";
        }

        try {
            restaurantService.deleteRestaurantById(id); // Method to delete restaurant
            redirectAttributes.addFlashAttribute("successMessage", "Restaurant deleted successfully!");
            return "redirect:/profile/" + user.getUsername(); // Redirect to the page listing restaurants
        } catch (Exception e) {
            System.out.print(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting restaurant.");
            return "redirect:/profile/" + user.getUsername();
        }
    }

}
