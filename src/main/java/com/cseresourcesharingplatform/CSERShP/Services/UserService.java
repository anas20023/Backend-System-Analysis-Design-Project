package com.cseresourcesharingplatform.CSERShP.Services;

import com.cseresourcesharingplatform.CSERShP.DTOs.UserResponseDTO;
import com.cseresourcesharingplatform.CSERShP.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.cseresourcesharingplatform.CSERShP.Repository.UserRepository;
import com.cseresourcesharingplatform.CSERShP.Entity.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAllUsers();

        // Convert each User → UserResponseDTO using stream
        return users.stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }


    public Optional<User> seeAllUsers(Long id) {
        return userRepository.seeAllUsers(id);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already taken!");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException("Username already taken!");
        }
        user.setPwhash(passwordEncoder.encode(user.getPwhash()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        return userRepository.findById(id)
                .map(existing -> {
                    existing.setFullName(updatedUser.getFullName());
                    existing.setProfileImageLink(updatedUser.getProfileImageLink());
                    return userRepository.save(existing);
                })
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public UserResponseDTO getMyProfile(HttpServletRequest request) throws ResourceNotFoundException {
        // 1. Get token from Authorization header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("JWT token not found in Authorization header");
        }
        String token = authHeader.substring(7); // remove "Bearer "

        // 2. Extract username from token
        String username = jwtService.extractUsername(token);

        // 3. Fetch user from DB
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // 4. Map to DTO
        return new UserResponseDTO(user);

    }
    public UserResponseDTO getUserWithResources(Long userId) {
        User user = null;
        try {
            user = userRepository.findByIdWithResources(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new UserResponseDTO(user);
    }
}