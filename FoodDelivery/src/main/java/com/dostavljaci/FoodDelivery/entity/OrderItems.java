package com.dostavljaci.FoodDelivery.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
public class OrderItems {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderID", nullable = false)
    private Orders order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MenuItemID", nullable = false)
    private MenuItems menuItem;
}
