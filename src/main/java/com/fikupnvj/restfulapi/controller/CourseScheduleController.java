package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.CourseSchedule;
import com.fikupnvj.restfulapi.service.CourseScheduleService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/course-schedule")
public class CourseScheduleController {

    @Autowired
    private CourseScheduleService courseScheduleService;

    @GetMapping
    public ResponseEntity<Object> getAll(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token) {
        return courseScheduleService.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return courseScheduleService.getById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token,
                                         @RequestParam(name = "courseName", required = false, defaultValue = "") String courseName,
                                         @RequestParam(name = "semester", required = false, defaultValue = "0") int semester,
                                         @RequestParam(name = "studyProgram", required = false, defaultValue = "") String studyProgram,
                                         @RequestParam(name = "academicPeriod", required = false, defaultValue = "") String academicPeriod,
                                         @RequestParam(name = "room", required = false, defaultValue = "") String room,
                                         @RequestParam(name = "lecturerName", required = false, defaultValue = "") String lecturerName,
                                         @RequestParam(name = "sortBy", required = false, defaultValue = "day") String sortBy,
                                         @RequestParam(name = "order", required = false, defaultValue = "asc") String order) {
        return courseScheduleService.search(courseName, semester, studyProgram, academicPeriod, room, lecturerName, sortBy, order);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @RequestBody CourseSchedule courseSchedule) {
        return courseScheduleService.create(courseSchedule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id, @RequestBody CourseSchedule courseSchedule) {
        return courseScheduleService.update(id, courseSchedule);
    }

    @PutMapping("/{id}/student/import")
    public ResponseEntity<Object> importStudentData(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id, @RequestParam("file") MultipartFile file) {
        return courseScheduleService.importStudentData(id, file);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return courseScheduleService.delete(id);
    }
}
