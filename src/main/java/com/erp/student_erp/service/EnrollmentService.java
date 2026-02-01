package com.erp.student_erp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erp.student_erp.entity.Course;
import com.erp.student_erp.entity.Enrollment;
import com.erp.student_erp.entity.User;
import com.erp.student_erp.repository.CourseRepository;
import com.erp.student_erp.repository.EnrollmentRepository;
import com.erp.student_erp.repository.UserRepository;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    public Enrollment enroll(String email, Long courseId) {
        User student = userRepository.findByEmail(email).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setCourse(course);

        return enrollmentRepository.save(enrollment);
    }
}
