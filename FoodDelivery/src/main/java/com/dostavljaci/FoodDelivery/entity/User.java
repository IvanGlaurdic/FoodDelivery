package com.dostavljaci.FoodDelivery.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String role;
}
