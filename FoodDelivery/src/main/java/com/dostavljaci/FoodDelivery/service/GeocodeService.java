package com.dostavljaci.FoodDelivery.service;

import com.dostavljaci.FoodDelivery.entity.Address;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import io.redlink.geocoding.Geocoder;
import io.redlink.geocoding.LatLon;
import io.redlink.geocoding.Place;


import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class GeocodeService {
    private Geocoder geocoder; // This bean is provided by the geocoding-spring-boot-autoconfigure


    public LatLon geocodeAddress(String address) throws IOException {
        List<Place> geocodedPlace = geocoder.geocode(address);
        if (!geocodedPlace.isEmpty()) {
            Place place = geocodedPlace.get(0); // Take the first result
            return place.getLatLon();
        }

        return null;
    }

    public Address reverseGeocodeLatLng(double latitude, double longitude) throws IOException {

            List<Place> reverseGeocodedPlaces = geocoder.reverseGeocode(LatLon.create(latitude, longitude));
            if (!reverseGeocodedPlaces.isEmpty()) {
                Place place = reverseGeocodedPlaces.get(0);
                Address address = new Address();

                String fullAddress = place.getAddress();

                String[] addressComponents = fullAddress.split(", ");
                address.setStreet(addressComponents[0]);
                address.setCity(addressComponents[1]);
                address.setProvince(addressComponents[2]);
                address.setCountry(addressComponents[addressComponents.length - 1]);
                address.setPostalCode(addressComponents[addressComponents.length - 2]);

                address.setLatitude((float) place.getLatLon().lat());
                address.setLongitude((float) place.getLatLon().lon());
                return address;
            }
        return null;
    }
}
