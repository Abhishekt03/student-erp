package com.erp.student_erp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.erp.student_erp.entity.Course;
import com.erp.student_erp.entity.Enrollment;
import com.erp.student_erp.entity.User;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // check duplicate enrollment
    boolean existsByStudentAndCourse(User student, Course course);

    // student → courses
    List<Enrollment> findByStudent(User student);

    // teacher → students
    List<Enrollment> findByCourse(Course course);
    
    @Query("select e.student from Enrollment e where e.course.id = :courseId")
    List<User> findStudentsByCourseId(Long courseId);

}
