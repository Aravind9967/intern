package com.Internproject.some.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Internproject.some.entity.User;
import com.Internproject.some.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
 @Autowired
    private final UserService userService;
 
 
    public UserController(UserService userService) {
        this.userService = userService;
    }
 
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody Map<String, Object> body) {
        String username  = (String) body.get("username");
        boolean isPremium = Boolean.TRUE.equals(body.get("isPremium"));
        User user = userService.createUser(username, isPremium);
        return ResponseEntity.ok(user);
    }
 
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
 
    
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                           @RequestBody Map<String, Object> body) {
        String username  = (String) body.get("username");
        boolean isPremium = Boolean.TRUE.equals(body.get("isPremium"));
        return ResponseEntity.ok(userService.updateUser(id, username, isPremium));
    }
 
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted.");
    }
}