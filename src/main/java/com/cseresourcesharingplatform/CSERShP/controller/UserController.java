package com.cseresourcesharingplatform.CSERShP.controller;

import com.cseresourcesharingplatform.CSERShP.DTOs.UserResponseDTO;
import com.cseresourcesharingplatform.CSERShP.Services.AuthService;
import com.cseresourcesharingplatform.CSERShP.Services.EmailService;
import com.cseresourcesharingplatform.CSERShP.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cseresourcesharingplatform.CSERShP.Services.UserService;
import com.cseresourcesharingplatform.CSERShP.Entity.User;

import java.util.List;
import java.util.Map;

// import java.util.Optional;
@CrossOrigin(origins = {"http://localhost:5173","https://sad.anasibnbelal.live"})
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private AuthService authService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private final UserService userService;
    @Autowired
    private JwtUtil jwtUtil;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ✅ Get all users
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    // ✅ Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.seeAllUsers(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    // ✅ Create new user
    @PostMapping("/new")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    // Login User Logic
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        String password = payload.get("password");

        try {
            User user = authService.login(email, password);

            // fetch role properly if you want (currently hardcoded)
            String role = "USER";

            String jwtToken = jwtUtil.generateToken(user.getUsername(), role);

            return ResponseEntity.ok(Map.of("message", "Login successful", "token", jwtToken, "role", role, "user", Map.of("username", user.getUsername(), "email", user.getEmail(), "fullName", user.getFullName(), "profileImageLink", user.getProfileImageLink())));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }


    // ✅ Update user
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }


    @PostMapping("/forgot")
    public ResponseEntity<String> forgotUser(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");

        // 1. Validate Email Existence (404 Not Found)
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.status(400).body("Email is required !");
        }

        if (!userService.existsByEmail(email)) {
            return ResponseEntity.status(404).body("User not found.");
        }

        try {
            // 2. Generate the security code

            // 3. Send the code to the user's email
            // We can simplify the response handling if we assume sentCodeToEmail
            // handles its own success/failure and returns a simple indicator or throws an exception on error.
            // Assuming it returns a ResponseEntity whose status we ignore, we can just call it:
            authService.sendCodeToEmail(email);

            // 4. Return success response (200 OK)
            // A clear, user-friendly message is returned on success.
            return ResponseEntity.status(200).body("Password reset code sent to your email.");

        } catch (Exception e) {
            // Log the exception for internal debugging
            // logger.error("Error sending password reset code for email: {}", email, e);

            // Return a 500 Internal Server Error for generic server/email sending failures
            return ResponseEntity.status(500).body("An error occurred while attempting to send the reset code.");
        }
    }

    // ✅ Delete user
    @DeleteMapping("/drop/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User has been successfully deleted");
    }

}