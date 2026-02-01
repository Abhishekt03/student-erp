


package com.erp.student_erp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.erp.student_erp.entity.Attendance;
import com.erp.student_erp.entity.Course;
import com.erp.student_erp.entity.Enrollment;
import com.erp.student_erp.entity.Marks;
import com.erp.student_erp.entity.Timetable;
import com.erp.student_erp.entity.User;
import com.erp.student_erp.repository.AttendanceRepository;
import com.erp.student_erp.repository.CourseRepository;
import com.erp.student_erp.repository.EnrollmentRepository;
import com.erp.student_erp.repository.MarksRepository;
import com.erp.student_erp.repository.TimetableRepository;
import com.erp.student_erp.repository.UserRepository;
import com.erp.student_erp.userDTO.StudentMarksResponse;
import com.erp.student_erp.userDTO.TimetableResponse;

@RestController
@RequestMapping("/api/student")
@CrossOrigin
public class StudentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private MarksRepository marksRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private TimetableRepository timetableRepository;



    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard(Authentication auth) {

        User student = userRepository.findByEmail(auth.getName()).orElseThrow();

        int courses = enrollmentRepository.findByStudent(student).size();
        int attendance = attendanceRepository.findByStudent(student).size();
        int marks = marksRepository.findByStudent(student).size();

        Map<String, Object> data = new HashMap<>();
        data.put("courses", courses);
        data.put("attendanceRecords", attendance);
        data.put("marksCount", marks);

        return ResponseEntity.ok(data);
    }
   

    
    @GetMapping("/my-courses")
    public ResponseEntity<?> getMyCourses(Authentication authentication) {

        User student = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow();

        List<Enrollment> enrollments = enrollmentRepository.findByStudent(student);

        List<Course> courses = enrollments.stream()
                .map(Enrollment::getCourse)
                .toList();

        return ResponseEntity.ok(courses);
    }
    @PostMapping("/profile/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody Map<String,String> req,
            Authentication auth) {
        return ResponseEntity.ok("Password updated");
    }
   

    @GetMapping("/attendance")
    public ResponseEntity<?> attendance(Authentication auth) {
        User student = userRepository.findByEmail(auth.getName()).orElseThrow();
        List<Attendance> list = attendanceRepository.findByStudent(student);
        return ResponseEntity.ok(list);
    }
    @GetMapping("/timetable")
    public ResponseEntity<?> getMyTimetable(Authentication auth) {

        User student = userRepository.findByEmail(auth.getName()).orElseThrow();

        List<TimetableResponse> response =
            enrollmentRepository.findByStudent(student)
            .stream()
            .map(Enrollment::getCourse)
            .flatMap(course -> timetableRepository.findByCourse(course).stream())
            .map(t -> new TimetableResponse(
                t.getCourse().getTitle(),
                t.getDay(),
                t.getStartTime().toString(),
                t.getEndTime().toString(),
                t.getRoom()
            ))
            .toList();

        return ResponseEntity.ok(response);
    }




    @GetMapping("/attendance/{courseId}")
    public List<Attendance> getAttendance(
            @PathVariable Long courseId,
            Authentication auth) {

        User student = userRepository.findByEmail(auth.getName()).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();

        return attendanceRepository.findByStudentAndCourse(student, course);
    }

    @GetMapping("/marks")
    public ResponseEntity<?> getMarks(Authentication auth) {

        User student = userRepository.findByEmail(auth.getName()).orElseThrow();

        List<StudentMarksResponse> list =
            marksRepository.findByStudent(student)
            .stream()
            .map(m -> new StudentMarksResponse(
                m.getCourse().getTitle(),
                m.getInternal(),
                m.getExternal(),
                m.getTotal(),
                m.getGrade()
            ))
            .toList();

        return ResponseEntity.ok(list);
    }

}

