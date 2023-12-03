package com.dostavljaci.FoodDelivery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Welcome to our Website!");
        return "home"; // This should match the name of your Thymeleaf template
    }


}
