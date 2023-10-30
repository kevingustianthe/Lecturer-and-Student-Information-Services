package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.Lecturer;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.LecturerActivityResponse;
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
    public ApiResponse<Lecturer> getMe(Account account) {
        return lecturerService.getMe(account);
    }

    @GetMapping("/{id}/activity")
    public ApiResponse<List<LecturerActivityResponse>> getLecturerActivity(@PathVariable String id) {
        return lecturerService.getLecturerActivity(id);
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
