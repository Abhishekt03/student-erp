package com.erp.student_erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.erp.student_erp.entity.Course;
import com.erp.student_erp.entity.Enrollment;
import com.erp.student_erp.entity.User;
import com.erp.student_erp.repository.CourseRepository;
import com.erp.student_erp.repository.EnrollmentRepository;
import com.erp.student_erp.repository.UserRepository;

@RestController
@RequestMapping("/api/student")
public class EnrollmentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @PostMapping("/enroll/{courseId}")
    public ResponseEntity<?> enroll(
            @PathVariable Long courseId,
            Authentication auth) {

        User student = userRepository
                .findByEmail(auth.getName())
                .orElseThrow();

        Course course = courseRepository
                .findById(courseId)
                .orElseThrow();

        // prevent duplicate enrollment
        if (enrollmentRepository
                .existsByStudentAndCourse(student, course)) {
            return ResponseEntity
                    .badRequest()
                    .body("Already enrolled");
        }

        Enrollment e = new Enrollment();
        e.setStudent(student);
        e.setCourse(course);

        enrollmentRepository.save(e);
        return ResponseEntity.ok("Enrolled successfully");
    }
}