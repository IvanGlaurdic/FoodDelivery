package com.dostavljaci.FoodDelivery.repository;

import com.dostavljaci.FoodDelivery.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
    List<OrderItem> getReferenceByOrderId(UUID id);

    OrderItem getReferenceByOrderIdAndMenuItemId(UUID id, UUID menuItemId);
}
