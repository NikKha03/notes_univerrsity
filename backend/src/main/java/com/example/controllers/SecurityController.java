package com.example.controllers;

import com.example.DTO.SigninRequest;
import com.example.DTO.SignupRequest;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.token.JwtCore;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notes")
@AllArgsConstructor
public class SecurityController {
    private AuthenticationManager authenticationManager;
    private JwtCore jwtCore;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @GetMapping("/info")
    ResponseEntity<?> info() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated() && authentication.getPrincipal() != "anonymousUser")
            return ResponseEntity.ok(authentication.getName() + ", добро пожаловать!");

        return ResponseEntity.ok("Добро пожаловать!");
    }

    @PostMapping("/signup")
    ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        if (userRepository.existsUserByUsername(signupRequest.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(" Пользователь с таким username уже существует");
        }

        if (userRepository.existsUserByEmail(signupRequest.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Пользователь с таким email уже существует");
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        userRepository.save(user);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", "Регистрация прошла успешно!");

        return ResponseEntity.ok(jsonObject.toString());
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

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", user.getUsername());
        jsonObject.put("accessToken", jwt);
        jsonObject.put("id", user.getUser_id());
        jsonObject.put("email", user.getEmail());
        jsonObject.put("role", user.getRole());

        return ResponseEntity.ok(jsonObject.toString());
    }
}
