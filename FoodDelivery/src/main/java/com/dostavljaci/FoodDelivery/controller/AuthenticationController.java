package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.entity.User;
import com.dostavljaci.FoodDelivery.repository.UserRepository;
import com.dostavljaci.FoodDelivery.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@AllArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    public String performLogin(@RequestParam String usernameOrEmail, @RequestParam String rawPassword){
        User user = authenticationService.authenticate(usernameOrEmail, rawPassword);
        if (user != null){
            return "redirect:/" + user.getId();
        }
        else {
            return "redirect:/login?error=true";
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
                                  @RequestParam String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            // Handle the case where passwords do not match
            return "redirect:/register?error=passwordsDontMatch";
        }

        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(authenticationService.getPasswordEncoder().encode(password));
        newUser.setRole("user");


        userRepository.save(newUser);

        // Redirect to a success page or login page after successful registration
        return "redirect:/login";
    }
}

