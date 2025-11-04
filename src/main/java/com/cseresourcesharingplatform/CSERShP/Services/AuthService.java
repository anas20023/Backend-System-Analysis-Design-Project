package com.cseresourcesharingplatform.CSERShP.Services;

import com.cseresourcesharingplatform.CSERShP.Entity.ForgotLog;
import com.cseresourcesharingplatform.CSERShP.Repository.ForgotLogRepository;
import com.cseresourcesharingplatform.CSERShP.Repository.UserRepository;
import com.cseresourcesharingplatform.CSERShP.Entity.User;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Random;

import static java.lang.System.currentTimeMillis;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ForgotLogRepository forgotLogRepository;

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPwhash())) {
            throw new RuntimeException("Invalid credentials");
        }
        return user;
    }

    public String generateCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    public ResponseEntity<String> sendCodeToEmail(String email) {
        // Generate a 6-digit verification code
        String code = generateCode();

        String html = """
                <html>
                    <body style="font-family: Arial, sans-serif; background-color: #f5f5f5; padding: 30px;">
                        <div style="max-width: 500px; margin: auto; background-color: #ffffff; border-radius: 10px; padding: 25px; box-shadow: 0px 3px 8px rgba(0,0,0,0.1);">
                            <h2 style="color: #2d6cdf; text-align: center;">Password Reset Code</h2>
                            <p style="font-size: 16px; color: #333;">Hey there 👋,</p>
                            <p style="font-size: 16px; color: #333;">
                                We received a request to reset your password. Use the code below to reset it:
                            </p>
                            <div style="text-align: center; margin: 25px 0;">
                                <div style="display: inline-block; background-color: #2d6cdf; color: white; font-size: 22px; letter-spacing: 5px; padding: 12px 24px; border-radius: 6px; font-weight: bold;">
                                    %s
                                </div>
                            </div>
                            <p style="font-size: 14px; color: #555;">
                                This code will expire in <b>2 minutes</b>. If you didn’t request a password reset, just ignore this email.
                            </p>
                            <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
                            <p style="text-align: center; font-size: 12px; color: #aaa;">
                                © %s YourAppName. All rights reserved.
                            </p>
                        </div>
                    </body>
                </html>
                """.formatted(code, java.time.Year.now());


        try {
            emailService.sendHtmlMail(email, "Password Reset Code", html);
            try {
                userRepository.insertRecoveryCode(email, code);
            } catch (Exception e) {
                System.out.println(e);
            }

            return ResponseEntity.ok("Verification code sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to send verification code: " + e.getMessage());
        }
    }

    @Transactional
    public void resetPassword(String email, String code, String newPassword) throws Exception {
        ForgotLog forgotLog = forgotLogRepository.findTopByEmailOrderByCreatedAtDesc(email);
        if (forgotLog == null) {
            throw new Exception("No reset request found for this email!");
        }

        // Code Expiry in 2 min validation

        if (!code.equals(forgotLog.getCode())) {
            throw new Exception("Wrong code!");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found"));

        if (passwordEncoder.matches(newPassword, user.getPwhash())) {
            throw new Exception("New password cannot be same as old password!");
        }

        String hashpw = passwordEncoder.encode(newPassword);
        userRepository.setNewPassword(email, hashpw);
    }
}
