package com.dostavljaci.FoodDelivery.entity;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Data;

@Data
@Table(name = "notification", schema = "public")
@Entity
public class Notification {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "message")
    private String message;

    @Column(name = "timestamp")
    private Date timestamp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Order order;

}
