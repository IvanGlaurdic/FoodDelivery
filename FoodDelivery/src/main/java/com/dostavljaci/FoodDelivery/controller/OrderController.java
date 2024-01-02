package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class OrderController {
    public final OrderService orderService;
}
