package com.dostavljaci.FoodDelivery.repository;

import com.dostavljaci.FoodDelivery.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {
    List<MenuItem> getMenuItemsByRestaurantId(UUID id);

    @Query("SELECT DISTINCT m.category FROM MenuItem m")
    List<String> findAllDistinctCategories();
}
