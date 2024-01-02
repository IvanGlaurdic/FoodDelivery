package com.dostavljaci.FoodDelivery.repository;

import com.dostavljaci.FoodDelivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsernameOrEmail(String username, String email);
    User findByUsername(String username);


    User getUserById(UUID userId);
}
