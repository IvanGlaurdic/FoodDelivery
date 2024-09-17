package com.dostavljaci.FoodDelivery.repository;

import com.dostavljaci.FoodDelivery.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<Restaurant, UUID> {

    // Find restaurants by the owner's ID
    @Query("SELECT r FROM Restaurant r WHERE r.owner.id = :ownerId")
    List<Restaurant> findByOwnerId(@Param("ownerId") UUID ownerId);


    @Query("SELECT r FROM Restaurant r JOIN FETCH r.address")
    List<Restaurant> findRestaurantsWithAddresses();

    Restaurant findRestaurantByName(String restaurantName);


    @Query("SELECT r FROM Restaurant r LEFT JOIN FETCH r.address WHERE r.name = :name")
    Restaurant getRestaurantByName(@Param("name") String name);

    Restaurant findByNameIgnoreCase(String name);

    @Query("SELECT DISTINCT r FROM Restaurant r " +
            "JOIN MenuItem m ON r.id = m.restaurant.id " +
            "WHERE m.category = :category")
    List<Restaurant> findRestaurantsByMenuItemCategory(String category);


}
