package com.dostavljaci.FoodDelivery.DTO;

import com.dostavljaci.FoodDelivery.entity.Order;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
@AllArgsConstructor
public class OrderDTO {
    private UUID id;
    private Date orderDate;
    private LocalDateTime scheduledDeliveryTime;
    private float totalAmount;
    private String status;
    private UUID userId;
    private UUID restaurantId;



}
