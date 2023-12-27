package com.dostavljaci.FoodDelivery.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.UUID;

@Data
@Table(name = "restaurant", schema = "public")
@Entity
public class Restaurant {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String contactNumber;

    @Column
    private Float rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "owner_id_id")
    private User owner;

}
