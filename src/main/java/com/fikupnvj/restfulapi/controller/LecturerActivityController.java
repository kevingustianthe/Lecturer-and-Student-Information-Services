package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.LecturerActivity;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.service.LecturerActivityService;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lecturer/activity")
public class LecturerActivityController {

    @Autowired
    private LecturerActivityService lecturerActivityService;

    @GetMapping
    public ApiResponse<List<LecturerActivity>> getAll(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token) {
        return lecturerActivityService.getAll();
    }

    @GetMapping("/{id}")
    public ApiResponse<LecturerActivity> getById(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return lecturerActivityService.getById(id);
    }

    @GetMapping("/search")
    public ApiResponse<List<LecturerActivity>> search(@Parameter(hidden = true) Account account, @RequestHeader("X-API-TOKEN") String token,
            @RequestParam(name = "status", required = false, defaultValue = "") LecturerActivity.Status status,
            @RequestParam(name = "name", required = false, defaultValue = "") String name,
            @RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "order", required = false, defaultValue = "desc") String order) {
        return lecturerActivityService.search(status, lecturerName, sortBy, order);
    }

    @PostMapping
    public ApiResponse<LecturerActivity> create(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @RequestBody LecturerActivity lecturerActivity) {
        return lecturerActivityService.create(lecturerActivity);
    }

    @PutMapping("/{id}")
    public ApiResponse<LecturerActivity> update(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id, @RequestBody LecturerActivity lecturerActivity) {
        return lecturerActivityService.update(id, lecturerActivity);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<LecturerActivity> delete(@Parameter(hidden = true) Account admin, @RequestHeader("X-API-TOKEN") String token, @PathVariable String id) {
        return lecturerActivityService.delete(id);
    }
}
