package com.erp.student_erp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.student_erp.entity.Course;
import com.erp.student_erp.entity.User;

public interface CourseRepository extends JpaRepository<Course, Long> {
	List<Course> findByTeacher(User teacher);

}
