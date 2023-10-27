package com.fikupnvj.restfulapi.controller;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.Student;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ApiResponse<List<Student>> getAll() {
        return studentService.getAll();
    }

    @GetMapping("/me")
    public ApiResponse<Student> getMe(Account account) {
        return studentService.getMe(account);
    }

    @PostMapping
    public ApiResponse<Student> create(@RequestBody Student student) {
        return studentService.create(student);
    }

    @PutMapping("/{id}")
    public ApiResponse<Student> update(@PathVariable String id, @RequestBody Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Student> delete(@PathVariable String id) {
        return studentService.delete(id);
    }
}
