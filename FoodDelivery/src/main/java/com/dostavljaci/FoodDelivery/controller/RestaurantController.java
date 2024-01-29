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

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

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

    @GetMapping("/edit-restaurant/{restaurantName}")
    public String editRestaurant(@PathVariable("restaurantName") String restaurantName,
                                 Model model){

        Restaurant restaurant = restaurantService.getRestaurantByName(restaurantName);
        if (restaurant == null) {
            model.addAttribute("error", "Restaurant not found");
            return "edit-restaurant";  // You might want to redirect to a different page or show an error message
        }
        model.addAttribute("restaurant", restaurant);
        return "edit-restaurant";

        }
    @PostMapping("/edit-restaurant/{restaurantName}")
    public String handleEditedRestaurant(@PathVariable("restaurantName") String requestedName,
                                         @RequestParam String Name,
                                         @RequestParam String ContactNumber,
                                         Model model,
                                         HttpSession session){
        System.out.print(requestedName);

        Restaurant currentRestaurant = restaurantService.getRestaurantByName(requestedName);
        System.out.print(currentRestaurant);

        if (currentRestaurant == null) {
            return "redirect:/";
        }

        if (restaurantService.isUsernameTaken(Name, currentRestaurant.getId())) {
            model.addAttribute("error", "Username is already taken");
            model.addAttribute("restaurant", currentRestaurant);
            return "redirect:/restaurant/edit-restaurant/" + requestedName;
        }

        boolean isUpdated = updateIfChanged(currentRestaurant::getName, currentRestaurant::setName, Name)
                | updateIfChanged(currentRestaurant::getContactNumber, currentRestaurant::setContactNumber, ContactNumber);

        if (isUpdated){
            restaurantService.saveRestaurant(currentRestaurant);
        }
        if (session.getAttribute("user") instanceof User sessionUser){
            return "redirect:/profile/" + sessionUser.getUsername();
        }
        return "redirect:/";


    }
    private boolean updateIfChanged(Supplier<String> getter, Consumer<String> setter, String newValue) {
        if (!Objects.equals(getter.get(), newValue)) {
            setter.accept(newValue);
            return true;
        }
        return false;
    }

}
