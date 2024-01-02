package com.dostavljaci.FoodDelivery.repository;

import com.dostavljaci.FoodDelivery.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
}
