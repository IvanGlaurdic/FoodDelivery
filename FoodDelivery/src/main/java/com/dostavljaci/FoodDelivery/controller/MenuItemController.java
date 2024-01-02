package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.service.MenuItemService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class MenuItemController {
    public final MenuItemService menuItemService;
}
