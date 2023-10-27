package com.fikupnvj.restfulapi.service;

import com.fikupnvj.restfulapi.entity.Subject;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public ApiResponse<List<Subject>> getAll() {
        return new ApiResponse<>(true, "Data successfully retrieved", subjectRepository.findAll());
    }

    public Subject findById(String id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Subject data not found"));
    }

    public ApiResponse<Subject> getById(String id) {
        Subject subject = findById(id);
        return new ApiResponse<>(true, "Data successfully retrieved", subject);
    }

    public ApiResponse<Subject> create(Subject subject) {
        return new ApiResponse<>(true, "Subject data has been successfully added", subjectRepository.save(subject));
    }

    public ApiResponse<Subject> update(String id, Subject subject) {
        findById(id);
        subject.setId(id);
        return new ApiResponse<>(true, "Subject data has been successfully updated", subjectRepository.save(subject));
    }

    public ApiResponse<Subject> delete(String id) {
        Subject subject = findById(id);
        subjectRepository.delete(subject);
        return new ApiResponse<>(true, "Subject data has been successfully deleted", subject);
    }
}
