package com.dostavljaci.FoodDelivery.entity;

import lombok.Data;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;
@Data
@Table(name = "user", schema = "public")
@Entity
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, name = "first_name")
    private String firstName;

    @Column(nullable = false, name = "last_name")
    private String lastName;

    @Column(nullable = false, unique = true, name = "username")
    private String username;

    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false, name = "role")
    private String role;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id")
    private Address address;
}
