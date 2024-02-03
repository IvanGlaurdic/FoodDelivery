package com.dostavljaci.FoodDelivery;

import com.dostavljaci.FoodDelivery.entity.Address;
import com.dostavljaci.FoodDelivery.repository.AddressRepository;
import com.dostavljaci.FoodDelivery.service.AddressService;
import com.dostavljaci.FoodDelivery.service.GeocodeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class AddressServiceTest {
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private GeocodeService geocodeService;
    @InjectMocks
    private AddressService addressService;

    @Test
    public void testSaveAddress() {
        Address address = new Address();
        address.setCity("TestCity");
        Map<String, Double> coords = new HashMap<>();
        coords.put("latitude", 10.0);
        coords.put("longitude", 20.0);
        when(geocodeService.geocodeAddress(any(String.class))).thenReturn(coords);
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        addressService.saveAddress(address);

        verify(addressRepository).save(any(Address.class));
    }

}
