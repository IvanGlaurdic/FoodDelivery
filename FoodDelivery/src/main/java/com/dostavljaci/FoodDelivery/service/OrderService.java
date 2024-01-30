package com.dostavljaci.FoodDelivery.service;

import com.ctc.wstx.shaded.msv_core.datatype.xsd.Base64BinaryType;
import com.dostavljaci.FoodDelivery.entity.MenuItem;
import com.dostavljaci.FoodDelivery.entity.Order;
import com.dostavljaci.FoodDelivery.entity.OrderItem;
import com.dostavljaci.FoodDelivery.repository.OrderItemRepository;
import com.dostavljaci.FoodDelivery.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Data
public class OrderService {
    private final OrderRepository orderRepository;
    private  final OrderItemRepository orderItemRepository;
    private final MenuItemService menuItemService;

    public void addMenuItemToOrder(Order order, UUID menuItemId) {
        // Assuming you have a method to fetch the MenuItem by its ID
        MenuItem menuItem = menuItemService.getMenuItemById(menuItemId);

        // Find an existing OrderItem in the order with the same MenuItem
        Optional<OrderItem> existingItem = order.getOrderItems().stream()
                .filter(item -> item.getMenuItem().getId().equals(menuItemId))
                .findFirst();


        if (existingItem.isPresent()) {
            // Increase quantity
            OrderItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + 1);
            orderItemRepository.save(item);
        } else {
            // Create a new OrderItem and add it to the order
            OrderItem newItem = new OrderItem();
            newItem.setMenuItem(menuItem);
            newItem.setQuantity(1);
            newItem.setOrder(order);
            orderItemRepository.save(newItem);
        }
    }


}
