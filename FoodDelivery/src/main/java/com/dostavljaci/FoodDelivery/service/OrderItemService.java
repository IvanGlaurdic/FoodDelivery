package com.dostavljaci.FoodDelivery.service;

import com.dostavljaci.FoodDelivery.repository.OrderItemRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Data
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

}
