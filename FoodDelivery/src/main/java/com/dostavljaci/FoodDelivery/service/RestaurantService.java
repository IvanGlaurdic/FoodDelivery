package com.dostavljaci.FoodDelivery.service;

import com.dostavljaci.FoodDelivery.entity.Address;
import com.dostavljaci.FoodDelivery.entity.Restaurant;
import com.dostavljaci.FoodDelivery.entity.User;
import com.dostavljaci.FoodDelivery.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.*;

@Data
@AllArgsConstructor
@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final GeocodeService geocodeService;

    public Map<Restaurant, Address> findClosestAddressesForAllRestaurants(Address userAddress) {
        List<Restaurant> restaurants = restaurantRepository.findAll();
        Map<Restaurant, Address> closestAddresses = new HashMap<>();

        for (Restaurant restaurant : restaurants) {
            Address closestAddress = null;
            double minDistance = Double.MAX_VALUE;

            for (Address address : restaurant.getAddress()) {
                Map<String, Object> distanceAndTime = geocodeService.calculateDistanceAndTime(
                        userAddress.getLatitude().toString(),
                        userAddress.getLongitude().toString(),
                        address.getLatitude().toString(),
                        address.getLongitude().toString()
                );

                double distance = (double) distanceAndTime.get("distance");
                if (distance < minDistance) {
                    minDistance = distance;
                    closestAddress = address;
                }
            }

            closestAddresses.put(restaurant, closestAddress);
        }

        return closestAddresses;
    }

    public List<Restaurant> getAllRestaurants(){
        return restaurantRepository.findAll();
    }


    public List<Restaurant> getRestaurantsByOwner(UUID ownerId) {
        return restaurantRepository.findByOwnerId(ownerId);
    }


    public Restaurant getRestaurantById(UUID restaurantId) {
        return restaurantRepository.getReferenceById(restaurantId);
    }

    public Restaurant getRestaurantByName(String restaurantName) {
        return restaurantRepository.getRestaurantByName(restaurantName);
    }

    public boolean isUsernameTaken(String restaurantName,UUID id) {
        Restaurant restaurant = restaurantRepository.getRestaurantByName(restaurantName);
        return restaurant != null && !restaurant.getId().equals(id);
    }


    public Restaurant saveRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Restaurant saveRestaurant(String restaurantName, String contactNumber, User user, float v, Address address, String fileName) {
        Restaurant restaurant;

        if (restaurantRepository.findRestaurantByName(restaurantName) == null){
            restaurant = new Restaurant();
            restaurant.setOwner(user);
            restaurant.setName(restaurantName);
            restaurant.setContactNumber(contactNumber);
            restaurant.setRating(v);
            List<Address> addresses = new ArrayList<>();
            addresses.add(address);
            restaurant.setAddress(addresses);
            restaurant.setPicturePath(fileName);
        }
        else {
            restaurant = restaurantRepository.findRestaurantByName(restaurantName);
            restaurant.getAddress().add(address);
        }

        return restaurantRepository.save(restaurant);
    }

    public List<Restaurant> getAllRestaurantsWithAddresses() {
        return restaurantRepository.findRestaurantsWithAddresses();
    }

    public void deleteRestaurantById(UUID id) {
         restaurantRepository.deleteById(id);
    }

    public Restaurant getRestaurantByNameIgnoreCase(String lowercaseQuery) {
        return restaurantRepository.findByNameIgnoreCase(lowercaseQuery);
    }

    public List<Restaurant> getRestaurantsByMenuItemCategory(String category) {
        return restaurantRepository.findRestaurantsByMenuItemCategory(category);
    }
}
