package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.Lecturer;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.LecturerActivityResponse;
import com.fikupnvj.restfulapi.model.CourseScheduleResponse;
import com.fikupnvj.restfulapi.model.LecturerResponse;
import com.fikupnvj.restfulapi.service.LecturerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lecturer")
public class LecturerController {

    @Autowired
    private LecturerService lecturerService;

    @GetMapping
    public ApiResponse<List<Lecturer>> getAll() {
        return lecturerService.getAll();
    }

    @GetMapping("/me")
    public ApiResponse<LecturerResponse> getMe(Account account) {
        return lecturerService.getMe(account);
    }

    @GetMapping("/me/activity")
    public ApiResponse<List<LecturerActivityResponse>> getMeLecturerActivity(Account account) {
        return lecturerService.getMeLecturerActivity(account);
    }

    @GetMapping("/me/course-schedule")
    public ApiResponse<List<CourseScheduleResponse>> getMeLecturerCourseSchedule(Account account) {
        return lecturerService.getMeLecturerCourseSchedule(account);
    }

    @GetMapping("/{id}")
    public ApiResponse<LecturerResponse> getById(@PathVariable String id) {
        return lecturerService.getById(id);
    }

    @GetMapping("/search")
    public ApiResponse<List<Lecturer>> getByParam(
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "studyProgram", required = false, defaultValue = "") String studyProgram,
            @RequestParam(name = "expertise", required = false, defaultValue = "") String expertise) {
        return lecturerService.getByParam(name, studyProgram, expertise);
    }

    @GetMapping("/{id}/activity")
    public ApiResponse<List<LecturerActivityResponse>> getLecturerActivity(@PathVariable String id) {
        return lecturerService.getLecturerActivity(id);
    }

    @GetMapping("/{id}/course-schedule")
    public ApiResponse<List<CourseScheduleResponse>> getLecturerCourseSchedule(@PathVariable String id) {
        return lecturerService.getLecturerCourseSchedule(id);
    }

    @PostMapping
    public ApiResponse<Lecturer> create(@RequestBody Lecturer lecturer) {
        return lecturerService.create(lecturer);
    }

    @PutMapping("/{id}")
    public ApiResponse<Lecturer> update(@PathVariable String id, @RequestBody Lecturer lecturer) {
        return lecturerService.update(id, lecturer);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Lecturer> delete(@PathVariable String id) {
        return lecturerService.delete(id);
    }
}
