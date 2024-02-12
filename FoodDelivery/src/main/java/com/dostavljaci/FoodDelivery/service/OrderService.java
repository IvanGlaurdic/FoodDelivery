package com.dostavljaci.FoodDelivery.service;

import com.dostavljaci.FoodDelivery.entity.*;
import com.dostavljaci.FoodDelivery.repository.OrderItemRepository;
import com.dostavljaci.FoodDelivery.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
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
        MenuItem menuItem = menuItemService.getMenuItemById(menuItemId);
        // Fetch the order from the database or get a managed entity
        Order managedOrder = orderRepository.findById(order.getId()).orElseThrow(() -> new EntityNotFoundException("Order not found"));

        // Check if OrderItem already exists and update or create accordingly
        OrderItem orderItem = managedOrder.getOrderItems().stream()
                .filter(item -> item.getMenuItem().getId().equals(menuItemId))
                .findFirst()
                .orElseGet(() -> {
                    OrderItem newItem = new OrderItem();
                    newItem.setMenuItem(menuItem);
                    newItem.setOrder(managedOrder);
                    newItem.setQuantity(0);
                    managedOrder.getOrderItems().add(newItem);
                    return newItem;
                });


        orderItem.setQuantity(orderItem.getQuantity() + 1);
        orderItemRepository.save(orderItem);

        // Recalculate total amount after adding the item
        recalculateTotalAmount(managedOrder);


        return orderRepository.save(managedOrder);

    }


    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order getOrderById(UUID id) {
        return orderRepository.getReferenceById(id);
    }

    @Transactional
    public Order setNewOrder(Date date, User user, Restaurant restaurant) {
        Order order = new Order();
        order.setOrderDate(date);
        order.setStatus("ordering");
        order.setUser(user);
        order.setRestaurant(restaurant);
        order.setTotalAmount(0);
        return orderRepository.save(order);
    }

    public LocalDateTime calculateScheduledDeliveryTime(Restaurant restaurant, Address userAddress) {
        long estimatedTimeInMillis = geocodeService.getScheduledDeliveryTime(restaurant, userAddress);
        // Convert milliseconds to minutes or your desired unit
        long estimatedTimeInMinutes = TimeUnit.MILLISECONDS.toMinutes(estimatedTimeInMillis);

        // Add the estimated time to the current time to get the scheduled delivery time
        return LocalDateTime.now().plusMinutes(estimatedTimeInMinutes + 20);
    }

    @Transactional
    public Order removeMenuItemFromOrder(Order order, UUID menuItemId) {
        // Ensure that we're working with a managed Order entity
        Order managedOrder = orderRepository.findById(order.getId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        // Find the specific order item
        Optional<OrderItem> optionalOrderItem = managedOrder.getOrderItems().stream()
                .filter(orderItem -> menuItemId.equals(orderItem.getMenuItem().getId()))
                .findFirst();

        if (optionalOrderItem.isPresent()) {
            OrderItem orderItem = optionalOrderItem.get();
            if (orderItem.getQuantity() > 1) {
                // If more than one quantity, reduce by one
                orderItem.setQuantity(orderItem.getQuantity() - 1);
                orderItemRepository.save(orderItem);
            } else {
                // If quantity is 1, remove the order item
                managedOrder.getOrderItems().remove(orderItem);
                orderItemRepository.delete(orderItem);
            }
            // Recalculate total amount after adding the item
            recalculateTotalAmount(managedOrder);
        } else {
            // This might happen if the item was removed in a concurrent transaction
            System.out.println("OrderItem not found for deletion or already deleted: " + menuItemId);
        }

        // Recalculate and set the total amount for the order
        float totalAmount = managedOrder.getOrderItems().stream()
                .map(item -> item.getQuantity() * item.getMenuItem().getPrice())
                .reduce(0f, Float::sum);
        managedOrder.setTotalAmount(totalAmount);

        // Save the updated order
        return orderRepository.save(managedOrder);
    }

    
    private void recalculateTotalAmount(Order order) {
        float totalAmount = order.getOrderItems().stream()
                .map(item -> item.getQuantity() * item.getMenuItem().getPrice())
                .reduce(0f, Float::sum);
        order.setTotalAmount(totalAmount);
    }



    public void confirmOrder(Order order) {
        order.setStatus("processing");
        order.setRating((float) 0);
        orderRepository.save(order);
        orderRepository.deleteOrdersByStatus("ordering");

    }


    public List<Order> getUserOrders(UUID id) {
        return orderRepository.getReferenceByUserId(id);
    }

    public List<Order> getOrdersByRestaurant(Restaurant restaurant) {
        return orderRepository.getReferenceByRestaurantId(restaurant.getId());
    }
    public List<Order> getCompletedOrdersByRestaurant(Restaurant restaurant) {
        return orderRepository.getReferenceByRestaurantIdCompleted(restaurant.getId());
    }
    public List<Order> getProcessingOrdersByRestaurant(Restaurant restaurant) {
        return orderRepository.getReferenceByRestaurantIdProcessing(restaurant.getId());
    }


}
