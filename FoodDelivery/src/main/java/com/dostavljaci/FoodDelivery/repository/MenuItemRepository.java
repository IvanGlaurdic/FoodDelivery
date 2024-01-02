package com.dostavljaci.FoodDelivery.repository;

import com.dostavljaci.FoodDelivery.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {
}
