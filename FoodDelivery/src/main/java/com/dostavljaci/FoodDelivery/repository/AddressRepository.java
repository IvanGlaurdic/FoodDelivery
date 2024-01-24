package com.dostavljaci.FoodDelivery.repository;


import com.dostavljaci.FoodDelivery.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface AddressRepository extends JpaRepository<Address, UUID> {

    Address findAddressByCityAndStreetAndCountryAndPostalCode(String city, String street, String country, String postalCode);
}
