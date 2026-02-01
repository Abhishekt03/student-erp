package com.erp.student_erp.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.erp.student_erp.entity.User;
import com.erp.student_erp.repository.UserRepository;
import com.erp.student_erp.security.JwtUtil;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ REGISTER
    public void register(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // ✅ FIX: SET USERNAME IF NULL
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getEmail().split("@")[0]);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("ROLE_STUDENT");
        } else if (user.getRole().equalsIgnoreCase("STUDENT")) {
            user.setRole("ROLE_STUDENT");
        } else if (user.getRole().equalsIgnoreCase("TEACHER")) {
            user.setRole("ROLE_TEACHER");
        } else {
            throw new RuntimeException("Invalid role");
        }

        user.setEnabled(true);

        userRepository.save(user);
    }




    // ✅ SEND OTP (RESEND)
    public void sendOtp(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEnabled()) {
            throw new RuntimeException("User already verified");
        }

       
        userRepository.save(user);
    }

    // ✅ VERIFY OTP
    public void verifyOtp(String email, String otp) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.isEnabled()) {
            throw new RuntimeException("User already verified");
        }

        if (!otp.equals(user.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        user.setEnabled(true);
        user.setOtp(null);
        user.setOtpExpiry(null);
        userRepository.save(user);
    }

    // ✅ LOGIN
    public String login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.isEnabled()) {
            throw new RuntimeException("Please verify OTP first");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }

    // 🔁 COMMON OTP METHOD
  
}
