package com.crowdconnect.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crowdconnect.model.Role;
import com.crowdconnect.model.User;
import com.crowdconnect.service.UserService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @PostMapping("/assign-role/{userId}")
    public ResponseEntity<Void> assignRole(@PathVariable Long userId, @RequestBody String role) {
        userService.assignRole(userId, Role.valueOf(role.toUpperCase())); // Convert string to Role enum
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // Other admin functionalities
}
