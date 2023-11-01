package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.LecturerActivity;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.service.LecturerActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lecturer/activity")
public class LecturerActivityController {

    @Autowired
    private LecturerActivityService lecturerActivityService;

    @GetMapping
    public ApiResponse<List<LecturerActivity>> getAll() {
        return lecturerActivityService.getAll();
    }

    @GetMapping("/{id}")
    public ApiResponse<LecturerActivity> getById(@PathVariable String id) {
        return lecturerActivityService.getById(id);
    }

    @GetMapping("/search")
    public ApiResponse<List<LecturerActivity>> getByStatus(
            @RequestParam(name = "status", required = false, defaultValue = "") LecturerActivity.Status status,
            @RequestParam(name = "name", required = false, defaultValue = "") String name) {
        return lecturerActivityService.getByParam(status, name);
    }

    @PostMapping
    public ApiResponse<LecturerActivity> create(@RequestBody LecturerActivity lecturerActivity) {
        return lecturerActivityService.create(lecturerActivity);
    }

    @PutMapping("/{id}")
    public ApiResponse<LecturerActivity> update(@PathVariable String id, @RequestBody LecturerActivity lecturerActivity) {
        return lecturerActivityService.update(id, lecturerActivity);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<LecturerActivity> delete(@PathVariable String id) {
        return lecturerActivityService.delete(id);
    }
}
