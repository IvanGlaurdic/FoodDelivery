package com.dostavljaci.FoodDelivery.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Table(name = "order", schema = "public")
@Entity
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Order {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "scheduled_delivery_time")
    private LocalDateTime scheduledDeliveryTime;

    @Column(name = "total_amount")
    private float totalAmount;

    @Column(name = "status")
    private String status;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<OrderItem> orderItems = new ArrayList<>();

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", orderDate=" + orderDate +
                ", scheduledDeliveryTime=" + scheduledDeliveryTime +
                ", totalAmount=" + totalAmount +
                ", status='" + status + '\'' +
                // Avoid calling toString on the 'user' and 'restaurant' fields
                ", userId=" + (user != null ? user.getId() : null) +
                ", restaurantId=" + (restaurant != null ? restaurant.getId() : null) +
                // Avoid calling toString on the 'orderItems' collection
                ", orderItemsCount=" + (orderItems != null ? orderItems.size() : 0) +
                '}';
    }
}
