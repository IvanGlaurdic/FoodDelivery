package com.dostavljaci.FoodDelivery.service;

import com.dostavljaci.FoodDelivery.entity.Address;
import com.dostavljaci.FoodDelivery.repository.AddressRepository;
import io.redlink.geocoding.LatLon;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
@Data
public class AddressService {
    private final AddressRepository addressRepository;
    private final GeocodeService geocodeService;
    public Address saveAddress(Address address) throws IOException {
        LatLon latLon = geocodeService.geocodeAddress(address.toString());
        if (latLon != null) {
            address.setLatitude((float) latLon.lat());
            address.setLongitude((float) latLon.lon());
        }
        return addressRepository.save(address);
    }

    public Address reverseGeocodeLatLng(double latitude, double longitude) throws IOException {
        return geocodeService.reverseGeocodeLatLng(latitude, longitude);
    }
}
