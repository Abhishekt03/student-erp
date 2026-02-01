package com.erp.student_erp.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
@RestController
@RequestMapping("/api/teacher")
@CrossOrigin
public class TeacherController {

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
    
    
    
    @PostMapping("/profile/change-password")
    public ResponseEntity<?> changePassword(
            @RequestBody Map<String,String> req,
            Authentication auth) {
        return ResponseEntity.ok("Password updated");
    }
    


    @GetMapping("/courses")
    public ResponseEntity<?> getTeacherCourses(Authentication auth) {

        User teacher = userRepository
                .findByEmail(auth.getName())
                .orElseThrow();

        return ResponseEntity.ok(
                courseRepository.findByTeacher(teacher)
        );
    }


    @PostMapping("/courses")
    public Course addCourse(@RequestBody Course course, Authentication auth) {
        User teacher = userRepository.findByEmail(auth.getName()).orElseThrow();
        course.setTeacher(teacher);
        return courseRepository.save(course);
    }


    @GetMapping("/students")
    public List<User> getStudentsByCourse(@RequestParam Long courseId) {
        return enrollmentRepository.findStudentsByCourseId(courseId);
    }


    // ✅ MARK ATTENDANCE
    @PostMapping("/attendance")
    public ResponseEntity<?> markAttendance(
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @RequestParam Boolean status
    ) {
        User student = userRepository.findById(studentId).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();

        LocalDate today = LocalDate.now();

        Optional<Attendance> existing =
            attendanceRepository.findByStudentAndCourseAndDate(student, course, today);

        if (existing.isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Attendance already marked for today");
        }

        Attendance attendance = new Attendance();
        attendance.setStudent(student);
        attendance.setCourse(course);
        attendance.setDate(today);
        attendance.setTime(LocalTime.now()); // ⏰ FIX
        attendance.setStatus(status);

        attendanceRepository.save(attendance);
        return ResponseEntity.ok("Attendance marked");
    }

    @Autowired
    private TimetableRepository timetableRepository;

    @PostMapping("/timetable")
    public ResponseEntity<?> addTimetable(
            @RequestParam Long courseId,
            @RequestParam String day,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam String room) {

        Course course = courseRepository.findById(courseId).orElseThrow();

        Timetable t = new Timetable();
        t.setCourse(course);
        t.setDay(day);
        t.setStartTime(startTime);
        t.setEndTime(endTime);
        t.setRoom(room);

        timetableRepository.save(t);

        return ResponseEntity.ok("Timetable added");
    }

    // ✅ ADD MARKS (FIXED)
    @PostMapping("/marks")
    public ResponseEntity<?> addMarks(
            @RequestParam Long studentId,
            @RequestParam Long courseId,
            @RequestParam int internal,
            @RequestParam int external) {

        User student = userRepository.findById(studentId).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();

        Marks m = new Marks();
        m.setStudent(student);
        m.setCourse(course);
        m.setInternal(internal);
        m.setExternal(external);
        m.setTotal(internal + external);
        m.setGrade(calculateGrade(internal + external));

        marksRepository.save(m);

        return ResponseEntity.ok("Marks added successfully");
    }

    // ✅ GRADE LOGIC
    private String calculateGrade(int total) {
        if (total >= 90) return "A+";
        if (total >= 80) return "A";
        if (total >= 70) return "B";
        if (total >= 60) return "C";
        if (total >= 50) return "D";
        return "F";
    }
}
