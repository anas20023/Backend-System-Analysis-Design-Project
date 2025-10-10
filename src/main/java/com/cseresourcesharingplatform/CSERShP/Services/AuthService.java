package com.cseresourcesharingplatform.CSERShP.Services;

import com.cseresourcesharingplatform.CSERShP.Repository.UserRepository;
import com.cseresourcesharingplatform.CSERShP.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPwhash())) {
            throw new RuntimeException("Invalid credentials");
        }
        return user;
    }

    public String generateCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    public ResponseEntity<Optional> sentCodeToEmail(String s, String email) {
        System.out.println(email+" "+s);
        return null;
    }
}
