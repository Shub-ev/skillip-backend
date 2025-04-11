package com.skillip.skillip_backend.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.skillip.skillip_backend.common.dtos.UserDTO;
import com.skillip.skillip_backend.models.User;
import com.skillip.skillip_backend.services.UserService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping("/")
    public ResponseEntity<UserDTO> createUser(@RequestBody User user){
        UserDTO savedUser = userService.createUser(user);
        System.out.println(savedUser);

        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/{email}")
    public User getUser(@PathVariable String email) {
        return userService.getUser(email);
    }

    @PostMapping("/become_talent")
    public User createTalent(@RequestBody User user){
        return userService.createTalent(user);
    }

    @PostMapping("/{email}/profile-image")
    public ResponseEntity<Map<String, Object>> updateProfileImage(@PathVariable String email, @RequestParam("file") MultipartFile file){
        return userService.uploadProfileImage(email, file);
    }
}
