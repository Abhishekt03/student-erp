package com.erp.student_erp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class EmailOtp {

    @Id
    private String email;

    private String otp;
    private LocalDateTime expiry;
}
