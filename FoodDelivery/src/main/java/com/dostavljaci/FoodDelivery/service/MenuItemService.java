package com.dostavljaci.FoodDelivery.service;

import com.dostavljaci.FoodDelivery.repository.MenuItemRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Data
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;
}
