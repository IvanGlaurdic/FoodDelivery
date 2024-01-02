package com.dostavljaci.FoodDelivery.service;

import com.dostavljaci.FoodDelivery.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Data
public class OrderService {
    private final OrderRepository orderRepository;
}
