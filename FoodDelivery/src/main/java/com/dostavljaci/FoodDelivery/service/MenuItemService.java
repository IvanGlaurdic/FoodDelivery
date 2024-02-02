package com.dostavljaci.FoodDelivery.service;

import com.dostavljaci.FoodDelivery.entity.MenuItem;
import com.dostavljaci.FoodDelivery.repository.MenuItemRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Data
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;

    public void deleteMenuItemById(UUID id) {
        menuItemRepository.deleteById(id);
    }

    public List<MenuItem> getMenuByRestaurantId(UUID id) {
        return menuItemRepository.getMenuItemsByRestaurantId(id);
    }

    public MenuItem getMenuItemById(UUID menuItemId) {
        return menuItemRepository.getReferenceById(menuItemId);
    }

    public List<String> getAllUniqueCategories() {
        return menuItemRepository.findAllDistinctCategories();
    }


    public void saveMenuItem(MenuItem menuItem) {
        menuItemRepository.save(menuItem);
    }
}
