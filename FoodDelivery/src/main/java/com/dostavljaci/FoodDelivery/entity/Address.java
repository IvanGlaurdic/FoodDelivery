package com.dostavljaci.FoodDelivery.entity;

import lombok.Data;
import jakarta.persistence.*;

import java.util.List;
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

    @Column( name = "province")
    private String province;

    @Column(nullable = false, name = "country")
    private String country;

    @Column(nullable = false, name = "postal_code")
    private String postalCode;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "longitude")
    private Float longitude;

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %s", street,city,province,country,postalCode);
    }

}
