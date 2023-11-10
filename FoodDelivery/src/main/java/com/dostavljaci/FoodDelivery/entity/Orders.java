package com.dostavljaci.FoodDelivery.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Orders {
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
    @JoinColumn(name = "UserID")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RestaurantID")
    private Restaurants restaurant;

}
