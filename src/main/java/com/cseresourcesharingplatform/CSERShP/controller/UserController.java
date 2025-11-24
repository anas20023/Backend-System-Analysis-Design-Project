package com.cseresourcesharingplatform.CSERShP.controller;

import com.cseresourcesharingplatform.CSERShP.DTOs.UserResponseDTO;
import com.cseresourcesharingplatform.CSERShP.Services.AuthService;
import com.cseresourcesharingplatform.CSERShP.Services.EmailService;
import com.cseresourcesharingplatform.CSERShP.Services.ResourceNotFoundException;
import com.cseresourcesharingplatform.CSERShP.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cseresourcesharingplatform.CSERShP.Services.UserService;
import com.cseresourcesharingplatform.CSERShP.Entity.User;

import java.util.List;
import java.util.Map;

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

    //  Get all users
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(HttpServletRequest request) throws ResourceNotFoundException {
        //System.out.println(request.getHeader("Authorization"));
       try{
           return ResponseEntity.ok(userService.getMyProfile(request));
       }catch (Exception e){
           throw  new ResourceNotFoundException(e.getMessage());
       }
    }
//    @PatchMapping("/me")
//    public ResponseEntity<UserResponseDTO> updateCurrentUser(HttpServletRequest request) throws ResourceNotFoundException {
//
//    }
    //  Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.seeAllUsers(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    //  Create new user
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


    //  Update user
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }


    @PostMapping("/forgot")
    public ResponseEntity<String> forgotUser(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");

        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.status(400).body("Email is required !");
        }

        if (!userService.existsByEmail(email)) {
            return ResponseEntity.status(404).body("User not found.");
        }

        try {
            authService.sendCodeToEmail(email);
            return ResponseEntity.status(200).body("Password reset code sent to your email.");

        } catch (Exception e) {

            return ResponseEntity.status(500).body("An error occurred while attempting to send the reset code.");
        }
    }

    @PostMapping("/newpassword")
    public ResponseEntity<String> newPassword(@RequestBody Map<String, String> payload) throws Exception {
        String code= payload.get("otp");
        String email= payload.get("email");
        String newPassword= payload.get("newPassword");
        try {
            authService.resetPassword(email,code,newPassword);
            return ResponseEntity.status(200).body("Password Reset Successfully !");
        }
        catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    //  Delete user
    @DeleteMapping("/drop/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User has been successfully deleted");
    }

}