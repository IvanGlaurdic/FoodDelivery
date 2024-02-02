package com.dostavljaci.FoodDelivery.repository;

import com.dostavljaci.FoodDelivery.entity.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Order o WHERE o.status = :status")
    void deleteOrdersByStatus(@Param("status") String status);

    List<Order> getReferenceByUserId(UUID id);

    List<Order> getReferenceByRestaurantId(UUID id);
}
