package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.entity.User;
import com.dostavljaci.FoodDelivery.service.AuthenticationService;
import com.dostavljaci.FoodDelivery.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public String performLogin(@RequestParam String usernameOrEmail, @RequestParam String rawPassword, Model model, HttpSession session){
        User user = authenticationService.authenticate(usernameOrEmail, rawPassword);
        if (user != null){
            session.setAttribute("user", user);
            return "redirect:/";
        }
        else {
            model.addAttribute("error","Incorrect username or password");
            return "login";
        }
    }

    @GetMapping("/register")
    public String register(){
       return "register";
    }

    @PostMapping("/register")
    public String performRegister(@RequestParam String firstName,
                                  @RequestParam String lastName,
                                  @RequestParam String username,
                                  @RequestParam String email,
                                  @RequestParam String password,
                                  @RequestParam String confirmPassword,
                                  @RequestParam String phoneNumber,
                                  Model model) {
        if (!password.equals(confirmPassword)) {
            // Handle the case where passwords do not match
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        userService.saveNewUser(firstName,
                lastName,
                username,
                email,
                authenticationService.getPasswordEncoder().encode(password),
                phoneNumber,
                null);


        // Redirect to a success page or login page after successful registration
        return "redirect:/login";
    }
}

