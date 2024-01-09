package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.Lecturer;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.LecturerActivityResponse;
import com.fikupnvj.restfulapi.model.CourseScheduleResponse;
import com.fikupnvj.restfulapi.model.LecturerResponse;
import com.fikupnvj.restfulapi.service.LecturerService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/lecturer")
public class LecturerController {

    @Autowired
    private LecturerService lecturerService;

    @GetMapping
    public ApiResponse<List<Lecturer>> getAll(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token) {
        return lecturerService.getAll();
    }

    @GetMapping("/me")
    public ApiResponse<LecturerResponse> getMe(@Parameter(hidden = true) Account lecturer, @RequestHeader("X-API-TOKEN") String token) {
        return lecturerService.getMe(lecturer);
    }

    @GetMapping("/me/activity")
    public ApiResponse<List<LecturerActivityResponse>> getMeLecturerActivity(@Parameter(hidden = true) Account lecturer, @RequestHeader("X-API-TOKEN") String token) {
        return lecturerService.getMeLecturerActivity(lecturer);
    }

    @GetMapping("/me/course-schedule")
    public ApiResponse<List<CourseScheduleResponse>> getMeLecturerCourseSchedule(@Parameter(hidden = true) Account lecturer, @RequestHeader("X-API-TOKEN") String token) {
        return lecturerService.getMeLecturerCourseSchedule(lecturer);
    }

    @GetMapping("/{id}")
    public ApiResponse<LecturerResponse> getById(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return lecturerService.getById(id);
    }

    @GetMapping("/search")
    public ApiResponse<List<Lecturer>> getByParam(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "studyProgram", required = false, defaultValue = "") String studyProgram,
            @RequestParam(name = "expertise", required = false, defaultValue = "") String expertise,
            @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
            @RequestParam(name = "order", required = false, defaultValue = "desc") String order) {
        return lecturerService.getByParam(name, studyProgram, expertise, sortBy, order);
    }

    @GetMapping("/{id}/activity")
    public ApiResponse<List<LecturerActivityResponse>> getLecturerActivity(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return lecturerService.getLecturerActivity(id);
    }

    @GetMapping("/{id}/course-schedule")
    public ApiResponse<List<CourseScheduleResponse>> getLecturerCourseSchedule(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return lecturerService.getLecturerCourseSchedule(id);
    }

    @PostMapping
    public ApiResponse<Lecturer> create(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @RequestBody Lecturer lecturer) {
        return lecturerService.create(lecturer);
    }

    @PostMapping("/import")
    public ApiResponse<List<Lecturer>> importData(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @RequestParam("file") MultipartFile file) {
        return lecturerService.importData(file);
    }

    @PutMapping("/{id}")
    public ApiResponse<Lecturer> update(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id, @RequestBody Lecturer lecturer) {
        return lecturerService.update(id, lecturer);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Lecturer> delete(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return lecturerService.delete(id);
    }
}
