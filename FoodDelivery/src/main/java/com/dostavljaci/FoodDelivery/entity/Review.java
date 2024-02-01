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

    @Column(name = "rating")
    private int rating;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;
}
