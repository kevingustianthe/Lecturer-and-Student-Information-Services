package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.Course;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.service.CourseService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ApiResponse<List<Course>> getAll(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token) {
        return courseService.getAll();
    }

    @GetMapping("/{id}")
    public ApiResponse<Course> getById(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return courseService.getById(id);
    }

    @GetMapping("/search")
    public ApiResponse<List<Course>> search(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "semester", required = false, defaultValue = "0") int semester,
            @RequestParam(name = "studyProgram", required = false, defaultValue = "") String studyProgram,
            @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
            @RequestParam(name = "order", required = false, defaultValue = "desc") String order) {
        return courseService.search(name, semester, studyProgram, sortBy, order);
    }

    @PostMapping
    public ApiResponse<Course> create(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @RequestBody Course course) {
        return courseService.create(course);
    }

    @PutMapping("/{id}")
    public ApiResponse<Course> update(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id, @RequestBody Course course) {
        return courseService.update(id, course);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Course> delete(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return courseService.delete(id);
    }

}
