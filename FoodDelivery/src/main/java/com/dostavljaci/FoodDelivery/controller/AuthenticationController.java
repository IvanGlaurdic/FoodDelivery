package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.entity.Address;
import com.dostavljaci.FoodDelivery.entity.User;
import com.dostavljaci.FoodDelivery.service.AddressService;
import com.dostavljaci.FoodDelivery.service.AuthenticationService;
import com.dostavljaci.FoodDelivery.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;
    private final AddressService addressService;

    @GetMapping("/login")
    public String login(){
        return "login-form";
    }

    @PostMapping("/login")
    public String performLogin(@RequestParam String usernameOrEmail, @RequestParam String rawPassword, Model model, HttpSession session){
        User user = authenticationService.authenticate(usernameOrEmail, rawPassword);

        if (user != null){
            session.setAttribute("user", user);
            model.addAttribute("isLoggedIn", session.getAttribute("isLoggedIn"));
            return "redirect:/";
        }
        else {
            model.addAttribute("error","Incorrect username or password");
            return "login-form";
        }
    }

    @GetMapping("/register")
    public String register(){
       return "registration-form";
    }


    @PostMapping("/register")
    public String handleUserRegistration(@ModelAttribute User user,
                                         @RequestParam String confirmPassword,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {


        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "registration-form";
        }
        if (userService.getUserByUsername(user.getUsername())!= null){
            model.addAttribute("error", "Username already taken.");
            return "registration-form";
        }
        if (userService.getUserByEmail(user.getEmail())!= null){
            model.addAttribute("error", "Email already taken.");
            return "registration-form";
        }

        redirectAttributes.addFlashAttribute("user", user);
        return "redirect:/address-form";
    }

    @GetMapping("/address-form")
    public String showAddressForm(Model model) {
        if (!model.containsAttribute("user")) {
            return "redirect:/register";
        }
        return "address-form";
    }

    @PostMapping("/create-account")
    public String createAccount(@ModelAttribute User user, @ModelAttribute Address address,HttpSession session) {

        userService.saveNewUser(
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail(),
                authenticationService.getPasswordEncoder().encode(user.getPassword()),
                user.getPhoneNumber(),
                addressService.getAddressByAllParams(
                        address.getCity(),
                        address.getStreet(),
                        address.getProvince(),
                        address.getCountry(),
                        address.getPostalCode())
        );
        return "redirect:/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}

