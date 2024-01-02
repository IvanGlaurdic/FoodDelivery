package com.dostavljaci.FoodDelivery.controller;

import com.dostavljaci.FoodDelivery.entity.Address;
import com.dostavljaci.FoodDelivery.service.AddressService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

@Controller
@AllArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @PostMapping("/address")
    public ResponseEntity<Address> createAddress(@RequestBody Address address) throws IOException, InterruptedException {
        Address savedAddress = addressService.saveAddress(address);
        return new ResponseEntity<>(savedAddress, HttpStatus.CREATED);
    }

    @GetMapping("/address")
    public ResponseEntity<Address> getAddress(@RequestParam double latitude, @RequestParam double longitude) throws Exception {
        Address address = addressService.reverseGeocodeLatLng((float) latitude, (float) longitude);
        if (address != null) {
            return new ResponseEntity<>(address, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
