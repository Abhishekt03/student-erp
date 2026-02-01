package com.erp.student_erp.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.erp.student_erp.entity.User;
import com.erp.student_erp.repository.UserRepository;
import com.erp.student_erp.userDTO.ProfileResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    @Autowired
    private UserRepository userRepository;

    // ✅ PROFILE DETAILS
    @GetMapping
    public ResponseEntity<?> profile(Authentication auth) {

        User u = userRepository.findByEmail(auth.getName()).orElseThrow();

        Map<String, Object> res = new HashMap<>();
        res.put("id", u.getId());
        res.put("name", u.getName());
        res.put("email", u.getEmail());
        res.put("role", u.getRole());
        res.put("dob", u.getDob());
        res.put("phone", u.getPhone());
        res.put("address", u.getAddress());

        return ResponseEntity.ok(res);
    }

    // ✅ GET PHOTO
    @GetMapping("/photo")
    public ResponseEntity<byte[]> getPhoto(Authentication auth) throws IOException {

        User user = userRepository.findByEmail(auth.getName()).orElseThrow();

        byte[] photo = user.getPhoto();

        if (photo == null) {
            InputStream is = getClass()
                    .getResourceAsStream("/static/images/default.png");
            photo = is.readAllBytes();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(photo);
    }

    // ✅ UPLOAD PHOTO
    @PostMapping("/photo")
    public ResponseEntity<?> upload(
            @RequestParam("photo") MultipartFile file,
            Authentication auth) throws IOException {

        User user = userRepository.findByEmail(auth.getName()).orElseThrow();
        user.setPhoto(file.getBytes());
        userRepository.save(user);

        return ResponseEntity.ok("Photo updated");
    }
}
