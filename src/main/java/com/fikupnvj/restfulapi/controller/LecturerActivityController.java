package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Account;
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
    public ApiResponse<List<LecturerActivity>> getAll(Account account) {
        return lecturerActivityService.getAll();
    }

    @GetMapping("/{id}")
    public ApiResponse<LecturerActivity> getById(Account account, @PathVariable String id) {
        return lecturerActivityService.getById(id);
    }

    @GetMapping("/search")
    public ApiResponse<List<LecturerActivity>> getByStatus(Account account,
            @RequestParam(name = "status", required = false, defaultValue = "") LecturerActivity.Status status,
            @RequestParam(name = "name", required = false, defaultValue = "") String name) {
        return lecturerActivityService.getByParam(status, name);
    }

    @PostMapping
    public ApiResponse<LecturerActivity> create(Account admin, @RequestBody LecturerActivity lecturerActivity) {
        return lecturerActivityService.create(lecturerActivity);
    }

    @PutMapping("/{id}")
    public ApiResponse<LecturerActivity> update(Account admin, @PathVariable String id, @RequestBody LecturerActivity lecturerActivity) {
        return lecturerActivityService.update(id, lecturerActivity);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<LecturerActivity> delete(Account admin, @PathVariable String id) {
        return lecturerActivityService.delete(id);
    }
}
