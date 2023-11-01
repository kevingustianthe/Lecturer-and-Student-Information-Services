package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.CourseSchedule;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.CourseScheduleResponse;
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
    public ApiResponse<List<CourseScheduleResponse>> getAll() {
        return courseScheduleService.getAll();
    }

    @GetMapping("/{id}")
    public ApiResponse<CourseScheduleResponse> getById(@PathVariable String id) {
        return courseScheduleService.getById(id);
    }

    @GetMapping("/search")
    public ApiResponse<List<CourseScheduleResponse>> getByParam(
            @RequestParam(name = "courseName", required = false, defaultValue = "") String courseName,
            @RequestParam(name = "semester", required = false, defaultValue = "0") int semester,
            @RequestParam(name = "studyProgram", required = false, defaultValue = "") String studyProgram,
            @RequestParam(name = "academicPeriod", required = false, defaultValue = "") String academicPeriod,
            @RequestParam(name = "room", required = false, defaultValue = "") String room,
            @RequestParam(name = "lecturerName", required = false, defaultValue = "") String lecturerName) {
        return courseScheduleService.getByParam(courseName, semester, studyProgram, academicPeriod, room, lecturerName);
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
