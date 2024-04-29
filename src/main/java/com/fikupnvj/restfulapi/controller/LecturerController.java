package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.Lecturer;
import com.fikupnvj.restfulapi.service.LecturerService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/lecturer")
public class LecturerController {

    @Autowired
    private LecturerService lecturerService;

    @GetMapping
    public ResponseEntity<Object> getAll(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token) {
        return lecturerService.getAll();
    }

    @GetMapping("/me")
    public ResponseEntity<Object> getMe(@Parameter(hidden = true) Account lecturer, @RequestHeader("X-API-TOKEN") String token) {
        return lecturerService.getMe(lecturer);
    }

    @GetMapping("/me/activity")
    public ResponseEntity<Object> getMeLecturerActivity(@Parameter(hidden = true) Account lecturer, @RequestHeader("X-API-TOKEN") String token) {
        return lecturerService.getMeLecturerActivity(lecturer);
    }

    @GetMapping("/me/course-schedule")
    public ResponseEntity<Object> getMeLecturerCourseSchedule(@Parameter(hidden = true) Account lecturer, @RequestHeader("X-API-TOKEN") String token) {
        return lecturerService.getMeLecturerCourseSchedule(lecturer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return lecturerService.getById(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getByParam(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token,
                                             @RequestParam(name = "name", required = false, defaultValue = "") String name,
                                             @RequestParam(name = "studyProgram", required = false, defaultValue = "") String studyProgram,
                                             @RequestParam(name = "expertise", required = false, defaultValue = "") String expertise,
                                             @RequestParam(name = "sortBy", required = false, defaultValue = "id") String sortBy,
                                             @RequestParam(name = "order", required = false, defaultValue = "desc") String order) {
        return lecturerService.getByParam(name, studyProgram, expertise, sortBy, order);
    }

    @GetMapping("/{id}/activity")
    public ResponseEntity<Object> getLecturerActivity(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return lecturerService.getLecturerActivity(id);
    }

    @GetMapping("/{id}/course-schedule")
    public ResponseEntity<Object> getLecturerCourseSchedule(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return lecturerService.getLecturerCourseSchedule(id);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @RequestBody Lecturer lecturer) {
        return lecturerService.create(lecturer);
    }

    @PostMapping("/import")
    public ResponseEntity<Object> importData(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @RequestParam("file") MultipartFile file) {
        return lecturerService.importData(file);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id, @RequestBody Lecturer lecturer) {
        return lecturerService.update(id, lecturer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return lecturerService.delete(id);
    }
}
