package com.dostavljaci.FoodDelivery.repository;

import com.dostavljaci.FoodDelivery.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}
