package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.service.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class ReviewController {
    public final ReviewService reviewService;
}
