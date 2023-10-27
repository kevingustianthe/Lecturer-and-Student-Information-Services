package com.fikupnvj.restfulapi.service;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.Student;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public ApiResponse<List<Student>> getAll() {
        return new ApiResponse<>(true, "Data successfully retrieved", studentRepository.findAll());
    }

    public Student findStudentById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student data not found"));
    }

    public ApiResponse<Student> getMe(Account account) {
        Student student = studentRepository.findByEmail(account.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student data not found"));

        if (student.getAccount() == null) {
            student.setAccount(account);
            studentRepository.save(student);
        }

        return new ApiResponse<>(true, "Data successfully retrieved", student);
    }

    public ApiResponse<Student> create(Student student) {
        studentRepository.save(student);
        return new ApiResponse<>(true, "Student data has been successfully added", student);
    }

    public ApiResponse<Student> update(String id, Student student) {
        findStudentById(id);
        student.setId(id);
        return new ApiResponse<>(true, "Student data has been successfully updated", studentRepository.save(student));
    }

    public ApiResponse<Student> delete(String id) {
        Student student = findStudentById(id);
        studentRepository.delete(student);
        return new ApiResponse<>(true, "Student data has been successfully deleted", student);
    }
}
