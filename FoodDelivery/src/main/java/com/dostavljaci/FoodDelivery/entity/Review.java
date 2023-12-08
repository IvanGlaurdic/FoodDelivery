package com.dostavljaci.FoodDelivery.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.UUID;

@Data
@Table(name = "review", schema = "public")
@Entity
public class Review {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private int rating;

    @Column
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "orderid")
    private Order order;
}
