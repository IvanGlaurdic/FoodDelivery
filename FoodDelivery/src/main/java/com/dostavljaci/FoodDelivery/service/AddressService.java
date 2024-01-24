package com.dostavljaci.FoodDelivery.service;

import com.dostavljaci.FoodDelivery.entity.Address;
import com.dostavljaci.FoodDelivery.repository.AddressRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
@Data
public class AddressService {
    private final AddressRepository addressRepository;
    private final GeocodeService geocodeService;
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
        if (addressRepository.findAddressByCityAndStreetAndCountryAndPostalCode(city,street,country,postalCode)==null )
            return saveAddress(city,street,province,country,postalCode);
        return addressRepository.findAddressByCityAndStreetAndCountryAndPostalCode(city,street,country,postalCode);
    }
}
