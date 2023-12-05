package com.dostavljaci.FoodDelivery.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Table(name = "order")
@Entity
public class Order {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private LocalDate orderDate;

    @Column
    private LocalDate scheduledDeliveryDate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, length = 36)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Restaurant restaurant;
}
