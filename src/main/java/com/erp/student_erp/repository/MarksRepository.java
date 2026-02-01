package com.erp.student_erp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.erp.student_erp.entity.Marks;
import com.erp.student_erp.entity.User;

public interface MarksRepository extends JpaRepository<Marks, Long> {
    List<Marks> findByStudent(User student);
}
