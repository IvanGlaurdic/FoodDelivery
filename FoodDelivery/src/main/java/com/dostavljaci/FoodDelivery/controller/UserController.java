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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;


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
            Model model,
            RedirectAttributes redirectAttributes) {
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
            redirectAttributes.addFlashAttribute("successMessage", null);
            redirectAttributes.addFlashAttribute("errorMessage", null);
            return "profile";
        } else {
            // User is not authenticated, redirect to login page
            return "redirect:/login";
        }
    }

    @GetMapping("/edit-user/{username}")
    public String editProfile( @PathVariable("username") String username,
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

    @PostMapping("/edit-user/{username}")
    public String handleEditedProfile(@PathVariable("username") String requestedUsername,
                                      @RequestParam String firstName,
                                      @RequestParam String lastName,
                                      @RequestParam String username,
                                      @RequestParam String email,
                                      @RequestParam String password,
                                      @RequestParam String confirmPassword,
                                      @RequestParam String phoneNumber,
                                      @RequestParam String role,
                                      @RequestParam String street,
                                      @RequestParam String city,
                                      @RequestParam String province,
                                      @RequestParam String country,
                                      @RequestParam String postalCode,
                                      Model model,
                                      HttpSession session) {

        User currentUser = userService.getUserByUsername(requestedUsername);

        if (currentUser == null) {
            return "redirect:/";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "edit-user/" + requestedUsername;
        }

        // Check for unique username and email
        if (userService.isUsernameTaken(username, currentUser.getId())) {
            model.addAttribute("error", "Username is already taken");
            return "edit-user/" + requestedUsername;
        }

        if (userService.isEmailTaken(email, currentUser.getId())) {
            model.addAttribute("error", "Email is already taken");
            return "edit-user";
        }

        boolean isUpdated = updateIfChanged(currentUser::getFirstName, currentUser::setFirstName, firstName)
                | updateIfChanged(currentUser::getLastName, currentUser::setLastName, lastName)
                | updateIfChanged(currentUser::getUsername, currentUser::setUsername, username)
                | updateIfChanged(currentUser::getEmail, currentUser::setEmail, email)
                | updatePasswordIfChanged(currentUser, password)
                | updateIfChanged(currentUser::getPhoneNumber, currentUser::setPhoneNumber, phoneNumber)
                | updateIfChanged(currentUser::getRole, currentUser::setRole, role);


        Address existingAddress = currentUser.getAddress();

        boolean isProvinceUpdated = false;
        if (province != null && !province.trim().isEmpty()) {
            isProvinceUpdated = updateIfChanged(existingAddress::getProvince, existingAddress::setProvince, province);
        }

        boolean isAddressUpdated = updateIfChanged(existingAddress::getStreet, existingAddress::setStreet, street)
                | updateIfChanged(existingAddress::getCity, existingAddress::setCity, city)
                | isProvinceUpdated
                | updateIfChanged(existingAddress::getCountry, existingAddress::setCountry, country)
                | updateIfChanged(existingAddress::getPostalCode, existingAddress::setPostalCode, postalCode);

        if (isAddressUpdated) {
            addressService.saveAddress(existingAddress);
        }

        if (isUpdated || isAddressUpdated) {
            userService.saveUser(currentUser);
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

    private boolean updatePasswordIfChanged(User user, String newPassword) {
        String encodedPassword = authenticationService.getPasswordEncoder().encode(newPassword);
        if (!encodedPassword.equals(user.getPassword())) {
            user.setPassword(encodedPassword);
            return true;
        }
        return false;
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
                                  Model model,
                                  HttpSession session ){
        Object sessionUser = session.getAttribute("user");
        User user1;

        if (sessionUser instanceof User userInstance){
            user1 = userService.getUserByUsername(userInstance.getUsername());
        }
        else {
            throw new RuntimeException("Cant locate user:" + sessionUser);
        }

        if (!password.equals(confirmPassword)) {
            // Handle the case where passwords do not match
            model.addAttribute("error", "Passwords do not match");
            return "add-user";
        }

        if (userService.getUserByUsername(username)==null){
            Address address = addressService.getAddressByAllParams(city,street,province,country,postalCode);
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



        return "redirect:/profile/" + user1.getUsername();

    }


    @PostMapping("/delete-user/{username}")
    public String deleteUser(@PathVariable("username") String username,
                             RedirectAttributes redirectAttributes,
                             HttpSession session) {

        Object sessionUser = session.getAttribute("user");

        if (sessionUser instanceof User sessionUserInstance) {
            User authenticatedUser = userService.getUserByUsername(sessionUserInstance.getUsername());

            // Check if the authenticated user is an admin or the user being deleted
            if (Objects.equals(authenticatedUser.getRole().toLowerCase(), "admin")
                    || Objects.equals(authenticatedUser.getUsername(), username)) {

                try {
                    // Get the user to be deleted
                    User userToDelete = userService.getUserByUsername(username);

                    // Set the address to null before deleting the user
                    if (userToDelete != null) {
                        userToDelete.setAddress(null);
                        userService.saveUser(userToDelete);
                        userService.deleteUserByUsername(username);
                        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
                    }
                } catch (Exception e) {
                    // Log the exception using a logging framework (e.g., SLF4J)

                    redirectAttributes.addFlashAttribute("errorMessage", "Error deleting user.");
                }

                if (Objects.equals(authenticatedUser.getRole().toLowerCase(), "admin")) {
                    return "redirect:/profile/" + authenticatedUser.getUsername();
                } else {
                    return "redirect:/logout"; // Redirect to logout for non-admin users
                }
            }
        }

        return "redirect:/";
    }




}
