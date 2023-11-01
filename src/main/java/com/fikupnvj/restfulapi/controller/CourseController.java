package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Course;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ApiResponse<List<Course>> getAll() {
        return courseService.getAll();
    }

    @GetMapping("/{id}")
    public ApiResponse<Course> getById(@PathVariable String id) {
        return courseService.getById(id);
    }

    @GetMapping("/search")
    public ApiResponse<List<Course>> getByParam(
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "semester", required = false, defaultValue = "0") int semester,
            @RequestParam(name = "studyProgram", required = false, defaultValue = "") String studyProgram) {
        return courseService.getByParam(name, semester, studyProgram);
    }

    @PostMapping
    public ApiResponse<Course> create(@RequestBody Course course) {
        return courseService.create(course);
    }

    @PutMapping("/{id}")
    public ApiResponse<Course> update(@PathVariable String id, @RequestBody Course course) {
        return courseService.update(id, course);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Course> delete(@PathVariable String id) {
        return courseService.delete(id);
    }

}
