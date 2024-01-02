package com.dostavljaci.FoodDelivery.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Table(name = "menuitem", schema = "public")
@Entity
public class MenuItem {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(nullable = false, name = "price")
    private float price;

    @Column(name = "image_url")
    private String imageURL;

    @Column(nullable = false, name = "category")
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "restaurant_id")
    private Restaurant restaurant;
}
