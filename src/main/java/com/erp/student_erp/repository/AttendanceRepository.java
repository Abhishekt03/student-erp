package com.erp.student_erp.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.student_erp.entity.Attendance;
import com.erp.student_erp.entity.Course;
import com.erp.student_erp.entity.User;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByStudent(User student);

    List<Attendance> findByStudentAndCourse(User student, Course course);
    
    Optional<Attendance> 
    findByStudentAndCourseAndDate(User student, Course course, LocalDate date);

}

