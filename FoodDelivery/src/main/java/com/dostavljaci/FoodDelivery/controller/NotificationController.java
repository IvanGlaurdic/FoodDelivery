package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class NotificationController {
    public final NotificationService notificationService;
}
