package com.erp.student_erp.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.erp.student_erp.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    
}
