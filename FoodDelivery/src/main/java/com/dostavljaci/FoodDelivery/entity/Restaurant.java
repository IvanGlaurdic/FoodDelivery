package com.dostavljaci.FoodDelivery.entity;

import lombok.Data;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Data
@Table(name = "restaurant", schema = "public")
@Entity
public class Restaurant {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, name = "name", unique = true)
    private String name;

    @Column(nullable = false, name = "contact_number")
    private String contactNumber;

    @Column(name = "rating")
    private Float rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "restaurant_addresses",
            joinColumns = @JoinColumn(name = "restaurant_id"),
            inverseJoinColumns = @JoinColumn(name = "address_id")
    )
    private List<Address> address;

}
