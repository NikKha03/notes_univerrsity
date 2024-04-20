package com.example.controllers;

import com.example.DTO.SigninRequest;
import com.example.DTO.SignupRequest;
import com.example.config.MyUserDetails;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.token.JwtCore;

import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/notes")
@AllArgsConstructor
public class SecurityController {
    private AuthenticationManager authenticationManager;
    private JwtCore jwtCore;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @GetMapping("/info")
    ResponseEntity<?> welcome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated())
            return ResponseEntity.ok(authentication.getName() + ", добро пожаловать!");

        return ResponseEntity.ok("Добро пожаловать!");
    }

    @PostMapping("/signup")
    ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        if (userRepository.existsUserByUsername(signupRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Cooose diferent username");
        }

        if (userRepository.existsUserByEmail(signupRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Cooose diferent email");
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setRole("USER");
        userRepository.save(user);

        return ResponseEntity.ok("Registered successfully!");
    }

    @PostMapping(value = "/signin", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> signin(@RequestBody SigninRequest signinRequest) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtCore.generateToken(authentication);
        User user = userRepository.getUserByUsername(signinRequest.getUsername());

        return ResponseEntity.ok(String.format("""
                { 
                    "username": "%s", 
                    "accessToken": "%s", 
                    "id": %d, 
                    "email": "%s", 
                    "role": "%s"
                }
                """, user.getUsername(), jwt, user.getUser_id(), user.getEmail(), user.getRole()));
    }
}
