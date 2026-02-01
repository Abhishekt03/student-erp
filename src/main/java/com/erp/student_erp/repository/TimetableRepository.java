package com.erp.student_erp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.student_erp.entity.Course;
import com.erp.student_erp.entity.Timetable;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByCourse(Course course);
}

