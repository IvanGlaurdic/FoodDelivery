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

    @Column(nullable = false)
    private Date orderDate;

    @Column
    private Date scheduledDeliveryDate;

    @Column(nullable = false)
    private float totalAmount;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Restaurant restaurant;
}
