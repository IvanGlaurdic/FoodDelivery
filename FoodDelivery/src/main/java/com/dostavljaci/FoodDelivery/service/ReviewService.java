package com.dostavljaci.FoodDelivery.service;

import com.dostavljaci.FoodDelivery.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Data
public class ReviewService {
    private final ReviewRepository reviewRepository;
}
