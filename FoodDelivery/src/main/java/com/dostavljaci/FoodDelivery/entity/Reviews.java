package com.dostavljaci.FoodDelivery.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Reviews {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private int rating;

    @Column
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "OrderID")
    private Orders order;
}
