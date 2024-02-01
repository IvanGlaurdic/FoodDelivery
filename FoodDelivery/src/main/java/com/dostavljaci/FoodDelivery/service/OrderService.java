package com.dostavljaci.FoodDelivery.service;

import com.ctc.wstx.shaded.msv_core.datatype.xsd.Base64BinaryType;
import com.dostavljaci.FoodDelivery.entity.*;
import com.dostavljaci.FoodDelivery.repository.OrderItemRepository;
import com.dostavljaci.FoodDelivery.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Data
public class OrderService {
    private final OrderRepository orderRepository;
    private  final OrderItemRepository orderItemRepository;
    private final MenuItemService menuItemService;
    private final GeocodeService geocodeService;

    private final OrderItemService orderItemService;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public Order addMenuItemToOrder(Order order, UUID menuItemId) {
        // Ensure the Order entity is managed
        if (!entityManager.contains(order)) {
            order = entityManager.merge(order);
        }

        // Find or create the orderItem for the menuItem
        Optional<OrderItem> existingItem = order.getOrderItems().stream()
                .filter(item -> menuItemId.equals(item.getMenuItem().getId()))
                .findFirst();

        OrderItem orderItem;
        if (existingItem.isPresent()) {
            // Item exists, increase quantity
            orderItem = existingItem.get();
            orderItem.setQuantity(orderItem.getQuantity() + 1);
        } else {
            // Item does not exist, create new
            orderItem = new OrderItem();
            MenuItem menuItem = menuItemService.getMenuItemById(menuItemId);
            orderItem.setMenuItem(menuItem);
            orderItem.setOrder(order);
            orderItem.setQuantity(1);
            order.getOrderItems().add(orderItem);
        }

        // Save the order item
        orderItemRepository.save(orderItem);

        // Recalculate the total amount for the order
        order.setTotalAmount((float) order.getOrderItems().stream()
                .mapToDouble(item -> item.getQuantity() * item.getMenuItem().getPrice())
                .sum());

        // Save the order
        return orderRepository.save(order);

    }


    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order getOrderById(UUID id) {
        return orderRepository.getReferenceById(id);
    }

    @Transactional
    public Order setNewOrder(Date date, String status, User user, Restaurant restaurant) {
        Order order = new Order();
        order.setOrderDate(date);
        order.setStatus(status);
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setScheduledDeliveryTime(calculateScheduledDeliveryTime(restaurant,user.getAddress()));
        order.setTotalAmount(0);
        return orderRepository.save(order);
    }

    LocalDateTime calculateScheduledDeliveryTime(Restaurant restaurant, Address userAddress) {
        long estimatedTimeInMillis = geocodeService.getClosestRestaurantAddress(restaurant, userAddress);
        // Convert milliseconds to minutes or your desired unit
        long estimatedTimeInMinutes = TimeUnit.MILLISECONDS.toMinutes(estimatedTimeInMillis);

        // Add the estimated time to the current time to get the scheduled delivery time
        return LocalDateTime.now().plusMinutes(estimatedTimeInMinutes);
    }
}
