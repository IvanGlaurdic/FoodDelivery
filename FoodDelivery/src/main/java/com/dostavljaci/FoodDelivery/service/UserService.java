package com.dostavljaci.FoodDelivery.service;

import com.dostavljaci.FoodDelivery.entity.Address;
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

    public void saveNewUser(String firstName,
                            String lastName,
                            String username,
                            String email,
                            String password,
                            String phoneNumber,
                            Address address) {

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        user.setRole("user");
        user.setAddress(address);
        userRepository.save(user);
    }

    public User getUserById(UUID userId) {
        return userRepository.getUserById(userId);
    }

    public User getUserByEmail(String email) {
        return userRepository.getUserByEmail(email);
    }

    public void updateUser(User currentUser) {
        userRepository.save(currentUser);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public boolean isUsernameTaken(String username, UUID userId) {
        User user = userRepository.findByUsername(username);
        return user != null && !user.getId().equals(userId);
    }
    public boolean isEmailTaken(String email, UUID userId) {
        User user = userRepository.findByEmail(email);
        return user != null && !user.getId().equals(userId);
    }
}
