package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.entity.Address;
import com.dostavljaci.FoodDelivery.entity.Restaurant;
import com.dostavljaci.FoodDelivery.entity.User;
import com.dostavljaci.FoodDelivery.service.AddressService;
import com.dostavljaci.FoodDelivery.service.AuthenticationService;
import com.dostavljaci.FoodDelivery.service.RestaurantService;
import com.dostavljaci.FoodDelivery.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;


@AllArgsConstructor
@Controller
@RequestMapping("/profile")
public class UserController {
    private final UserService userService;
    private final RestaurantService restaurantService;
    private final AuthenticationService authenticationService;
    private final AddressService addressService;

    @GetMapping("/{username}")
    public String profile(
            @PathVariable("username") String username,
            Model model) {
        User user = userService.getUserByUsername(username);
        if (user != null) {
            List<User> users;
            List<Restaurant> restaurants;
            if (Objects.equals(user.getRole().toLowerCase(), "admin")){
                users = userService.getAllUsers();
                restaurants = restaurantService.getAllRestaurantsWithAddresses();
            } else if (Objects.equals(user.getRole().toLowerCase(), "owner")) {
                restaurants = restaurantService.getRestaurantsByOwner(user.getId());
                users = null;
            }
            else {
                users = null;
                restaurants = null;
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

    @GetMapping("/edit-user")
    public String editProfile( String username,
                               Model model,
                               HttpSession session){
        Object sessionUser = session.getAttribute("user");

        if (sessionUser instanceof User userInstance){
            if (Objects.equals(userService.getUserByUsername(userInstance.getUsername()).getRole().toLowerCase(), "admin")
                || Objects.equals(userService.getUserByUsername(userInstance.getUsername()).getUsername(), username)
            ) {
                User user = userService.getUserByUsername(username);
                model.addAttribute("user", user);
                model.addAttribute("error", null);
                return "edit-user";
            }
        }
        return "redirect:/";
    }

    @PostMapping("/edit-user")
    public String handleEditedProfile(@RequestParam String firstName,
                                      @RequestParam String lastName,
                                      @RequestParam String username,
                                      @RequestParam String email,
                                      @RequestParam String password,
                                      @RequestParam String confirmPassword,
                                      @RequestParam String phoneNumber,
                                      @RequestParam UUID userId,
                                      @RequestParam String role,
                                      Model model) {


        User currentUser = userService.getUserById(userId);
        if (currentUser == null) {
            model.addAttribute("error", "User not found");
            return "edit-user";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "edit-user";
        }

        // Check if the username or email is occupied by another user
        User userWithSameUsername = userService.getUserByUsername(username);
        if (userWithSameUsername != null && !userWithSameUsername.getId().equals(userId)) {
            model.addAttribute("error", "Username is already taken");
            return "edit-user";
        }

        User userWithSameEmail = userService.getUserByEmail(email);
        if (userWithSameEmail != null && !userWithSameEmail.getId().equals(userId)) {
            model.addAttribute("error", "Email is already taken");
            return "edit-user";
        }

        // Update only the fields that have changed
        boolean isUpdated = false;
        if (!firstName.equals(currentUser.getFirstName())) {
            currentUser.setFirstName(firstName);
            isUpdated = true;
        }
        if (!lastName.equals(currentUser.getLastName())) {
            currentUser.setLastName(lastName);
            isUpdated = true;
        }

        if (!username.equals(currentUser.getUsername())) {
            currentUser.setUsername(username);
            isUpdated = true;
        }
        if (!email.equals(currentUser.getEmail())) {
            currentUser.setEmail(email);
            isUpdated = true;
        }
        String encodedPassword = authenticationService.getPasswordEncoder().encode(password);
        if (!encodedPassword.equals(currentUser.getPassword())) {
            currentUser.setPassword(encodedPassword);
            isUpdated = true;
        }

        if (!phoneNumber.equals(currentUser.getPhoneNumber())) {
            currentUser.setPhoneNumber(phoneNumber);
            isUpdated = true;
        }
        if (!role.equals(currentUser.getRole())) {
            currentUser.setRole(role);
            isUpdated = true;
        }
        // Similar checks and updates for username, email, password, phoneNumber

        if (isUpdated) {
            userService.updateUser(currentUser); // Assuming you have a method to save the user
        }

        return "redirect:/login";
    }

    @GetMapping("/add-user")
    public String addUser(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("address", new Address());
        model.addAttribute("error", null);
        return "add-user";
    }

    @PostMapping("/add-user")
    public String handleAddedUser(@RequestParam String firstName,
                                  @RequestParam String lastName,
                                  @RequestParam String username,
                                  @RequestParam String email,
                                  @RequestParam String password,
                                  @RequestParam String confirmPassword,
                                  @RequestParam String phoneNumber,
                                  @RequestParam String street,
                                  @RequestParam String city,
                                  @RequestParam String province,
                                  @RequestParam String country,
                                  @RequestParam String postalCode,
                                  Model model){

        if (!password.equals(confirmPassword)) {
            // Handle the case where passwords do not match
            model.addAttribute("error", "Passwords do not match");
            return "add-user";
        }

        if (userService.getUserByUsername(username)==null){
            Address address = addressService.getAddressByCityStreetCountryPostalCode(city,street,country,postalCode);
            if (address == null){
                address = addressService.saveAddress(city,street,province,country,postalCode);
            }

            userService.saveNewUser(
                    firstName,
                    lastName,
                    username,
                    email,
                    authenticationService.getPasswordEncoder().encode(password),
                    phoneNumber,
                    address);

            User user=userService.getUserByUsername(username);
            if (user.getAddress()==null){
                user.setAddress(address);
                userService.saveUser(user);
            }

            System.out.print(user);
        }

        model.addAttribute("error", null);



        return "home";

    }



}
