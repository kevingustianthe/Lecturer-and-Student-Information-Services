package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.CourseSchedule;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.service.CourseScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course-schedule")
public class CourseScheduleController {

    @Autowired
    private CourseScheduleService courseScheduleService;

    @GetMapping
    public ApiResponse<List<CourseSchedule>> getAll() {
        return courseScheduleService.getAll();
    }

    @GetMapping("/{id}")
    public ApiResponse<CourseSchedule> getById(@PathVariable String id) {
        return courseScheduleService.getById(id);
    }

    @PostMapping
    public ApiResponse<CourseSchedule> create(@RequestBody CourseSchedule courseSchedule) {
        return courseScheduleService.create(courseSchedule);
    }

    @PutMapping("/{id}")
    public ApiResponse<CourseSchedule> update(@PathVariable String id, @RequestBody CourseSchedule courseSchedule) {
        return courseScheduleService.update(id, courseSchedule);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<CourseSchedule> delete(@PathVariable String id) {
        return courseScheduleService.delete(id);
    }
}
