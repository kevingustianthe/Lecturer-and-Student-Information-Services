package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.Student;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.CourseScheduleResponse;
import com.fikupnvj.restfulapi.model.StudentResponse;
import com.fikupnvj.restfulapi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ApiResponse<List<Student>> getAll(Account account) {
        return studentService.getAll();
    }

    @GetMapping("/me")
    public ApiResponse<StudentResponse> getMe(Account student) {
        return studentService.getMe(student);
    }

    @GetMapping("/me/course-schedule")
    public ApiResponse<List<CourseScheduleResponse>> getMeCourseSchedule(Account student) {
        return studentService.getMeCourseSchedule(student);
    }

    @GetMapping("/me/course-schedule/today")
    public ApiResponse<List<CourseScheduleResponse>> getMeCourseScheduleToday(Account student) {
        return studentService.getMeCourseScheduleToday(student);
    }

    @GetMapping("/{id}")
    public ApiResponse<StudentResponse> getById(Account account, @PathVariable String id) {
        return studentService.getById(id);
    }

    @GetMapping("/search")
    public ApiResponse<List<Student>> getByParam(Account account,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "nim", required = false, defaultValue = "") String nim,
            @RequestParam(name = "classOf", required = false, defaultValue = "") String classOf,
            @RequestParam(name = "studyProgram", required = false, defaultValue = "") String studyProgram,
            @RequestParam(name = "interest", required = false, defaultValue = "") String interest,
            @RequestParam(name = "sortBy", required = false, defaultValue = "nim") String sortBy,
            @RequestParam(name = "order", required = false, defaultValue = "desc") String order) {
        return studentService.getByParam(name, nim, classOf, studyProgram, interest, sortBy, order);
    }

    @GetMapping("/{id}/course-schedule")
    public ApiResponse<List<CourseScheduleResponse>> getStudentCourseSchedule(Account account, @PathVariable String id) {
        return studentService.getStudentCourseSchedule(id);
    }

    @PostMapping
    public ApiResponse<Student> create(Account admin, @RequestBody Student student) {
        return studentService.create(student);
    }

    @PutMapping("/{id}")
    public ApiResponse<Student> update(Account admin, @PathVariable String id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Student> delete(Account admin, @PathVariable String id) {
        return studentService.delete(id);
    }
}
