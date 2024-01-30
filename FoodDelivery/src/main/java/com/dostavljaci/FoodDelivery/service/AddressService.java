package com.dostavljaci.FoodDelivery.service;

import com.dostavljaci.FoodDelivery.entity.Address;
import com.dostavljaci.FoodDelivery.entity.Restaurant;
import com.dostavljaci.FoodDelivery.repository.AddressRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
@Data
public class AddressService {
    private final AddressRepository addressRepository;
    private final GeocodeService geocodeService;
    private final RestaurantService restaurantService;
    public void saveAddress(Address address) {
        Map<String, Double> latLon = geocodeService.geocodeAddress(address.toString());
            address.setLatitude(latLon.get("latitude").floatValue());
            address.setLongitude(latLon.get("longitude").floatValue());

        addressRepository.save(address);
    }

    public Address saveAddress(String city, String street, String province, String country, String postalCode) {
        Address address = new Address();
        address.setCity(city);
        address.setStreet(street);
        address.setProvince(province);
        address.setCountry(country);
        address.setPostalCode(postalCode);

        Map<String, Double> latLon = geocodeService.geocodeAddress(address.toString());
        address.setLatitude(latLon.get("latitude").floatValue());
        address.setLongitude(latLon.get("longitude").floatValue());

        return addressRepository.save(address);
    }



    public Object getAddressById(UUID id) {
        return addressRepository.getReferenceById(id);
    }

    public Address getAddressByAllParams(String city, String street, String province, String country, String postalCode) {
        if(street!=null){
            if (addressRepository.findAddressByCityAndStreetAndCountryAndPostalCode(city,street,country,postalCode)==null )
                return saveAddress(city,street,province,country,postalCode);
            return addressRepository.findAddressByCityAndStreetAndCountryAndPostalCode(city,street,country,postalCode);
        }
        return null;
    }


    public void removeAddressFromRestaurant(UUID addressId, UUID restaurantId) {
        Address addressToRemove = addressRepository.findById(addressId).orElse(null);
        Restaurant restaurant = restaurantService.getRestaurantById(restaurantId);

        if ((long) restaurant.getAddress().size() ==1){
            return;
        }

        if (addressToRemove != null) {
            List<Address> updatedAddresses = restaurant.getAddress();

            // Remove the address from the list
            updatedAddresses.removeIf(a -> a.getId().equals(addressId));

            // Set the updated list of addresses and save the restaurant
            restaurant.setAddress(updatedAddresses);
            restaurantService.saveRestaurant(restaurant);
        }


    }

}
