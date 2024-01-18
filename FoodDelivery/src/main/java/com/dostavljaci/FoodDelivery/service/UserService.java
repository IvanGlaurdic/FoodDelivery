package com.dostavljaci.FoodDelivery.service;

import com.dostavljaci.FoodDelivery.entity.User;
import com.dostavljaci.FoodDelivery.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void saveUser(String firstName,
                         String lastName,
                         String username,
                         String email,
                         String password,
                         String phoneNumber) {

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        user.setRole("user");
        userRepository.save(user);
    }
}
