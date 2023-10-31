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
    public ApiResponse<List<Student>> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/me")
    public ApiResponse<StudentResponse> getMe(Account account) {
        return studentService.getMe(account);
    }

    @GetMapping("/me/course-schedule")
    public ApiResponse<List<CourseScheduleResponse>> getMeCourseSchedule(Account account) {
        return studentService.getMeCourseSchedule(account);
    }

    @GetMapping("/me/course-schedule/today")
    public ApiResponse<List<CourseScheduleResponse>> getMeCourseScheduleToday(Account account) {
        return studentService.getMeCourseScheduleToday(account);
    }

    @GetMapping("/{id}")
    public ApiResponse<StudentResponse> getById(@PathVariable String id) {
        return studentService.getById(id);
    }

    @GetMapping("/{id}/course-schedule")
    public ApiResponse<List<CourseScheduleResponse>> getStudentCourseSchedule(@PathVariable String id) {
        return studentService.getStudentCourseSchedule(id);
    }

    @PostMapping
    public ApiResponse<Student> create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @PutMapping("/{id}")
    public ApiResponse<Student> update(@PathVariable String id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Student> delete(@PathVariable String id) {
        return studentService.delete(id);
    }
}
