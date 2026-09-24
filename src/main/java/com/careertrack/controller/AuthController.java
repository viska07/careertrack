package com.careertrack.controller;

import com.careertrack.dto.LoginRequest;
import com.careertrack.dto.RegisterRequest;
import com.careertrack.entity.User;
import com.careertrack.repository.UserRepository;
import com.careertrack.service.JwtService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        if (userRepository
                .findByEmail(request.email())
                .isPresent()) {

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(
                            Map.of(
                                    "message",
                                    "Email sudah terdaftar"
                            )
                    );
        }

        User user = new User();

        user.setName(request.name().trim());
        user.setEmail(request.email().trim());
        user.setPassword(
                passwordEncoder.encode(
                        request.password()
                )
        );
        user.setCreatedAt(
                LocalDateTime.now()
        );

        User savedUser =
                userRepository.save(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        Map.of(
                                "message",
                                "Registrasi berhasil",
                                "userId",
                                savedUser.getId()
                        )
                );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request
    ) {

        User user =
                userRepository
                        .findByEmail(request.email().trim())
                        .orElse(null);

        if (user == null) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            Map.of(
                                    "message",
                                    "Email atau password salah"
                            )
                    );
        }

        boolean passwordMatch =
                passwordEncoder.matches(
                        request.password(),
                        user.getPassword()
                );

        if (!passwordMatch) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            Map.of(
                                    "message",
                                    "Email atau password salah"
                            )
                    );
        }

        String token =
                jwtService.generateToken(
                        user.getId(),
                        user.getEmail()
                );

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Login berhasil",
                        "token",
                        token,
                        "userId",
                        user.getId(),
                        "name",
                        user.getName(),
                        "email",
                        user.getEmail()
                )
        );
    }
}