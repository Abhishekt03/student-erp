package com.erp.student_erp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.erp.student_erp.entity.Course;
import com.erp.student_erp.entity.User;
import com.erp.student_erp.repository.CourseRepository;
import com.erp.student_erp.repository.UserRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	@Autowired
	 private  UserRepository userRepo;
	 @Autowired
	    private  PasswordEncoder encoder;

    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard() {
        return ResponseEntity.ok("Welcome Admin Dashboard");
    }
    
 

        @Autowired
        private CourseRepository courseRepository;

        
        
        @PostMapping("/teacher")
        public ResponseEntity<?> addTeacher(@RequestBody User u) {
            u.setRole("ROLE_TEACHER");
            u.setPassword(encoder.encode(u.getPassword()));
            userRepo.save(u);
            return ResponseEntity.ok("Teacher created");
        }

        @GetMapping("/users")
        public List<User> allUsers() {
            return userRepo.findAll();
        }
    }


