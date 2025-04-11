package com.skillip.skillip_backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillip.skillip_backend.common.dtos.LoginRequestDTO;
import com.skillip.skillip_backend.common.dtos.UserDTO;
import com.skillip.skillip_backend.services.AuthService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/user_auth/")
public class AuthController {

    @Autowired
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public UserDTO loginUser(@RequestBody LoginRequestDTO loginRequestDTO){
        return authService.loginUser(loginRequestDTO);
    }
}
