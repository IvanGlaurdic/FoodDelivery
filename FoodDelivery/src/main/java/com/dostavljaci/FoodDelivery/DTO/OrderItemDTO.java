package com.dostavljaci.FoodDelivery.DTO;

import com.dostavljaci.FoodDelivery.entity.OrderItem;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderItemDTO {
    private UUID id;
    private int quantity;
    private UUID orderId;
    private UUID menuItemId;
    private String menuItemName;
    private float price;

    public OrderItemDTO(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.quantity = orderItem.getQuantity();
        this.orderId = orderItem.getOrder() != null ? orderItem.getOrder().getId() : null;
        this.menuItemId = orderItem.getMenuItem() != null ? orderItem.getMenuItem().getId() : null;
        this.menuItemName = orderItem.getMenuItem() != null ? orderItem.getMenuItem().getName() : null;
        this.price= orderItem.getMenuItem() != null ? orderItem.getMenuItem().getPrice() : null;
    }
}
