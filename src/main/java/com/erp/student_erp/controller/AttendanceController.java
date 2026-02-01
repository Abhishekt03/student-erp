package com.erp.student_erp.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erp.student_erp.entity.Attendance;
import com.erp.student_erp.repository.AttendanceRepository;
import com.erp.student_erp.repository.CourseRepository;
import com.erp.student_erp.repository.UserRepository;

@RestController
@RequestMapping("/api/admin/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping
    public Attendance mark(@RequestParam String email,
                           @RequestParam Long courseId,
                           @RequestParam boolean present) {

    	Attendance a = new Attendance();
    	a.setStudent(userRepository.findByEmail(email).orElseThrow());
    	a.setCourse(courseRepository.findById(courseId).orElseThrow());
    	a.setDate(LocalDate.now());
    	a.setPresent(present);

    	attendanceRepository.save(a);


        return attendanceRepository.save(a);
    }
}
