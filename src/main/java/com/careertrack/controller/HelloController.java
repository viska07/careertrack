package com.careertrack.controller;

import com.careertrack.entity.User;
import com.careertrack.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HelloController {

    private final UserRepository userRepository;

    public HelloController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello from CareerTrack!";
    }

    @GetMapping("/api/users")
    public List<UserResponse> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getCreatedAt()
                ))
                .toList();
    }

    public record UserResponse(
            Long id,
            String name,
            String email,
            java.time.LocalDateTime createdAt
    ) {
    }
}