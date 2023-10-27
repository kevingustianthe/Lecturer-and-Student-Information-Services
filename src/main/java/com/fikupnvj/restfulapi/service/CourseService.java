package com.fikupnvj.restfulapi.service;

import com.fikupnvj.restfulapi.entity.Course;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public ApiResponse<List<Course>> getAll() {
        return new ApiResponse<>(true, "Data successfully retrieved", courseRepository.findAll());
    }

    public Course findById(String id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course data not found"));
    }

    public ApiResponse<Course> getById(String id) {
        Course course = findById(id);
        return new ApiResponse<>(true, "Data successfully retrieved", course);
    }

    public ApiResponse<Course> create(Course course) {
        return new ApiResponse<>(true, "Course data has been successfully added", courseRepository.save(course));
    }

    public ApiResponse<Course> update(String id, Course course) {
        findById(id);
        course.setId(id);
        return new ApiResponse<>(true, "Course data has been successfully updated", courseRepository.save(course));
    }

    public ApiResponse<Course> delete(String id) {
        Course course = findById(id);
        courseRepository.delete(course);
        return new ApiResponse<>(true, "Course data has been successfully deleted", course);
    }
}
