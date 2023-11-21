package com.dostavljaci.FoodDelivery.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
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
    private Float rating = 0.0f;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OwnerID", nullable = false)
    private User ownerId;

}
