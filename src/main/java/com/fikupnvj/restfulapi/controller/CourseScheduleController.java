package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.CourseSchedule;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.CourseScheduleResponse;
import com.fikupnvj.restfulapi.service.CourseScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/course-schedule")
public class CourseScheduleController {

    @Autowired
    private CourseScheduleService courseScheduleService;

    @GetMapping
    public ApiResponse<List<CourseScheduleResponse>> getAll(Account account) {
        return courseScheduleService.getAll();
    }

    @GetMapping("/{id}")
    public ApiResponse<CourseSchedule> getById(Account account, @PathVariable String id) {
        return courseScheduleService.getById(id);
    }

    @GetMapping("/search")
    public ApiResponse<List<CourseScheduleResponse>> getByParam(Account account,
            @RequestParam(name = "courseName", required = false, defaultValue = "") String courseName,
            @RequestParam(name = "semester", required = false, defaultValue = "0") int semester,
            @RequestParam(name = "studyProgram", required = false, defaultValue = "") String studyProgram,
            @RequestParam(name = "academicPeriod", required = false, defaultValue = "") String academicPeriod,
            @RequestParam(name = "room", required = false, defaultValue = "") String room,
            @RequestParam(name = "lecturerName", required = false, defaultValue = "") String lecturerName,
            @RequestParam(name = "sortBy", required = false, defaultValue = "day") String sortBy,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
        return courseScheduleService.getByParam(courseName, semester, studyProgram, academicPeriod, room, lecturerName, sortBy, order);
    }

    @PostMapping
    public ApiResponse<CourseSchedule> create(Account admin, @RequestBody CourseSchedule courseSchedule) {
        return courseScheduleService.create(courseSchedule);
    }

    @PutMapping("/{id}")
    public ApiResponse<CourseSchedule> update(Account admin, @PathVariable String id, @RequestBody CourseSchedule courseSchedule) {
        return courseScheduleService.update(id, courseSchedule);
    }

    @PutMapping("/{id}/student/import")
    public ApiResponse<CourseSchedule> updateCourseScheduleStudents(Account admin, @PathVariable String id, @RequestParam("file") MultipartFile file) {
        return courseScheduleService.updateCourseScheduleStudents(id, file);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<CourseSchedule> delete(Account admin, @PathVariable String id) {
        return courseScheduleService.delete(id);
    }
}
