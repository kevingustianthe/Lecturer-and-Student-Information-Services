package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Subject;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    @GetMapping
    public ApiResponse<List<Subject>> getAll() {
        return subjectService.getAll();
    }

    @GetMapping("/{id}")
    public ApiResponse<Subject> getById(@PathVariable String id) {
        return subjectService.getById(id);
    }

    @PostMapping
    public ApiResponse<Subject> create(@RequestBody Subject subject) {
        return subjectService.create(subject);
    }

    @PutMapping("/{id}")
    public ApiResponse<Subject> update(@PathVariable String id, @RequestBody Subject subject) {
        return subjectService.update(id, subject);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Subject> delete(@PathVariable String id) {
        return subjectService.delete(id);
    }

}
