package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.Course;
import com.fikupnvj.restfulapi.service.CourseService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public ResponseEntity<Object> getAll(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token) {
        return courseService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return courseService.getById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token,
                                         @RequestParam(name = "name", required = false, defaultValue = "") String name,
                                         @RequestParam(name = "semester", required = false, defaultValue = "0") int semester,
                                         @RequestParam(name = "studyProgram", required = false, defaultValue = "") String studyProgram,
                                         @RequestParam(name = "sortBy", required = false, defaultValue = "name") String sortBy,
                                         @RequestParam(name = "order", required = false, defaultValue = "desc") String order) {
        return courseService.search(name, semester, studyProgram, sortBy, order);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @RequestBody Course course) {
        return courseService.create(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id, @RequestBody Course course) {
        return courseService.update(id, course);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return courseService.delete(id);
    }

}
