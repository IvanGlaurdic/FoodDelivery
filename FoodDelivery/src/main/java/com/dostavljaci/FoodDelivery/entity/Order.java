package com.dostavljaci.FoodDelivery.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Table(name = "order", schema = "public")
@Entity
public class Order {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, name = "order_date")
    private Date orderDate;

    @Column(name = "scheduled_delivery_date")
    private Date scheduledDeliveryDate;

    @Column(nullable = false, name = "total_amount")
    private float totalAmount;

    @Column(nullable = false, name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;
}
