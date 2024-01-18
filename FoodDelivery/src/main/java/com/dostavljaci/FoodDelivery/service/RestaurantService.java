package com.dostavljaci.FoodDelivery.service;

import com.dostavljaci.FoodDelivery.entity.Restaurant;
import com.dostavljaci.FoodDelivery.entity.User;
import com.dostavljaci.FoodDelivery.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;

    public List<Restaurant> getAllRestaurants(){
        return restaurantRepository.findAll();
    }

    public List<Restaurant> getAllRestaurantsWithOwners() {
        return restaurantRepository.findAllWithOwners();
    }

    // Fetch restaurants owned by a user identified by UUID
    public List<Restaurant> getRestaurantsByOwner(UUID ownerId) {
        return restaurantRepository.findByOwnerId(ownerId);
    }


    public Restaurant getRestaurantById(UUID restaurantId) {
        return restaurantRepository.getReferenceById(restaurantId);
    }

    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Restaurant saveRestaurant(String restaurantName, String contactNumber, User user, float v) {
        Restaurant restaurant = new Restaurant();
        restaurant.setOwner(user);
        restaurant.setName(restaurantName);
        restaurant.setContactNumber(contactNumber);
        restaurant.setRating(v);
        return restaurantRepository.save(restaurant);
    }
}
