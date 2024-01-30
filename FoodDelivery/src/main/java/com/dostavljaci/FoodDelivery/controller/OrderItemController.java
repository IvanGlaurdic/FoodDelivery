package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.service.MenuItemService;
import com.dostavljaci.FoodDelivery.service.OrderItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class OrderItemController {
    public final OrderItemService orderItemService;

}
