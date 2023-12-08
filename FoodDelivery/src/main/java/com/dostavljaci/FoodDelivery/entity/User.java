package com.dostavljaci.FoodDelivery.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.UUID;
@Data
@Table(name = "user", schema = "public")
@Entity
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, name = "firstname")
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phoneNumber;

    @Column(nullable = false)
    private String role;
}
