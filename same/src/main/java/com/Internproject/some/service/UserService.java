package com.Internproject.some.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Internproject.some.entity.User;
import com.Internproject.some.repository.UserRepositories;


@Service
public class UserService {
 
    private final UserRepositories userRepository;
 
    public UserService(UserRepositories userRepository) {
        this.userRepository = userRepository;
    }
    public User createUser(String username, boolean isPremium) {
        User user = new User();
        user.setUsername(username);
        user.setPremium(isPremium);
        return userRepository.save(user);
    }
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
   public List<User> getAllUsers() {
        return userRepository.findAll(); 
    }
    public User updateUser(Long id, String username, boolean isPremium) {
        User user = getUserById(id);
        user.setUsername(username);
        user.setPremium(isPremium);
        return userRepository.save(user);
    }
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}