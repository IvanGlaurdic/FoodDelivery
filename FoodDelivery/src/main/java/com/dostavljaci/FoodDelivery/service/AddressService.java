package com.dostavljaci.FoodDelivery.service;

import com.dostavljaci.FoodDelivery.entity.Address;
import com.dostavljaci.FoodDelivery.repository.AddressRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
@AllArgsConstructor
@Data
public class AddressService {
    private final AddressRepository addressRepository;
    private final GeocodeService geocodeService;
    public Address saveAddress(Address address) throws IOException {
        Map<String, Double> latLon = geocodeService.geocodeAddress(address.toString());
        if (latLon != null) {
            address.setLatitude(latLon.get("lat").floatValue());
            address.setLongitude(latLon.get("lon").floatValue());
        }
        return addressRepository.save(address);
    }
}
