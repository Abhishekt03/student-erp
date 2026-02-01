package com.erp.student_erp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.student_erp.entity.User;
import com.erp.student_erp.repository.UserRepository;
import com.erp.student_erp.userDTO.StudentDashboardDTO;

@Service
public class StudentService {

    @Autowired
    private UserRepository userRepository;

    public StudentDashboardDTO getDashboard(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new StudentDashboardDTO(
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
