package com.erp.student_erp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.erp.student_erp.entity.Result;
import com.erp.student_erp.repository.CourseRepository;
import com.erp.student_erp.repository.ResultRepository;
import com.erp.student_erp.repository.UserRepository;

@RestController
@RequestMapping("/api/admin/result")
public class ResultController {

    @Autowired
    private ResultRepository resultRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @PostMapping
    public Result addResult(@RequestParam String email,
                            @RequestParam Long courseId,
                            @RequestParam int marks) {

        Result r = new Result();
        r.setStudent(userRepository.findByEmail(email).orElseThrow());
        r.setCourse(courseRepository.findById(courseId).orElseThrow());
        r.setMarks(marks);
        r.setGrade(marks >= 50 ? "PASS" : "FAIL");

        return resultRepository.save(r);
    }
}

