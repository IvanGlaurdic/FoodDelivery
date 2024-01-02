package com.dostavljaci.FoodDelivery.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.UUID;

@Data
@Table(name = "address", schema = "public")
@Entity
public class Address {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(nullable = false, name = "street")
    private String street;

    @Column(nullable = false, name = "city")
    private String city;

    @Column(nullable = false, name = "province")
    private String province;

    @Column(nullable = false, name = "country")
    private String country;

    @Column(nullable = false, name = "postal_code")
    private String postalCode;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Override
    public String toString() {
        return street + ", " + city + ", " + province + ", " + country + ", " + postalCode;
    }
}
