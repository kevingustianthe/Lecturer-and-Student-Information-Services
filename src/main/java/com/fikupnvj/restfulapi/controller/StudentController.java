package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.Student;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.CourseScheduleResponse;
import com.fikupnvj.restfulapi.model.StudentResponse;
import com.fikupnvj.restfulapi.service.StudentService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ApiResponse<List<Student>> getAll(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token) {
        return studentService.getAll();
    }

    @GetMapping("/me")
    public ApiResponse<StudentResponse> getMe(@Parameter(hidden = true) Account student, @RequestHeader("X-API-TOKEN") String token) {
        return studentService.getMe(student);
    }

    @GetMapping("/me/course-schedule")
    public ApiResponse<List<CourseScheduleResponse>> getMeCourseSchedule(@Parameter(hidden = true) Account student, @RequestHeader("X-API-TOKEN") String token) {
        return studentService.getMeCourseSchedule(student);
    }

    @GetMapping("/me/course-schedule/today")
    public ApiResponse<List<CourseScheduleResponse>> getMeCourseScheduleToday(@Parameter(hidden = true) Account student, @RequestHeader("X-API-TOKEN") String token) {
        return studentService.getMeCourseScheduleToday(student);
    }

    @GetMapping("/{id}")
    public ApiResponse<StudentResponse> getById(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return studentService.getById(id);
    }

    @GetMapping("/search")
    public ApiResponse<List<Student>> search(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "nim", required = false, defaultValue = "") String nim,
            @RequestParam(name = "classOf", required = false, defaultValue = "") String classOf,
            @RequestParam(name = "studyProgram", required = false, defaultValue = "") String studyProgram,
            @RequestParam(name = "interest", required = false, defaultValue = "") String interest,
            @RequestParam(name = "sortBy", required = false, defaultValue = "nim") String sortBy,
            @RequestParam(name = "order", required = false, defaultValue = "desc") String order) {
        return studentService.search(name, nim, classOf, studyProgram, interest, sortBy, order);
    }

    @GetMapping("/{id}/course-schedule")
    public ApiResponse<List<CourseScheduleResponse>> getStudentCourseSchedule(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return studentService.getStudentCourseSchedule(id);
    }

    @PostMapping
    public ApiResponse<Student> create(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @RequestBody Student student) {
        return studentService.create(student);
    }

    @PostMapping("/import")
    public ApiResponse<List<Student>> importData(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @RequestParam("file") MultipartFile file) {
        return studentService.importData(file);
    }

    @PutMapping("/{id}")
    public ApiResponse<Student> update(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Student> delete(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return studentService.delete(id);
    }
}
