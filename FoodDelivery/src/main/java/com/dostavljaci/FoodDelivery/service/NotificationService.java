package com.dostavljaci.FoodDelivery.service;

import com.dostavljaci.FoodDelivery.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Data
public class NotificationService {
    private final NotificationRepository notificationRepository;
}
