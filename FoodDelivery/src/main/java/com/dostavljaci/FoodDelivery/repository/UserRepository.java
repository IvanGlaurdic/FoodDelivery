package com.dostavljaci.FoodDelivery.repository;

import com.dostavljaci.FoodDelivery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsernameOrEmail(String username, String email);
    @Query("SELECT u FROM User u JOIN FETCH u.address WHERE u.username = :username")
    User findByUsername(String username);

    User getUserById(UUID userId);

    User getUserByEmail(String email);

    User findByEmail(String email);

}
