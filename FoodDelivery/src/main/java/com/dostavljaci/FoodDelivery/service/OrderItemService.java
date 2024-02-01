package com.dostavljaci.FoodDelivery.service;

import com.dostavljaci.FoodDelivery.entity.OrderItem;
import com.dostavljaci.FoodDelivery.repository.OrderItemRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
@Data
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public void saveNewOrderItem(OrderItem newOrderItem) {
        orderItemRepository.save(newOrderItem);
    }

    public OrderItem getOrderItemByOrderIdAndMenuItemId(UUID id, UUID menuItemId) {
        return orderItemRepository.getReferenceByOrderIdAndMenuItemId(id,menuItemId);
    }
}
