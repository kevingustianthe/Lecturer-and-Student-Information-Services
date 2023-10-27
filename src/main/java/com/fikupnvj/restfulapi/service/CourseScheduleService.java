package com.fikupnvj.restfulapi.service;

import com.fikupnvj.restfulapi.entity.CourseSchedule;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.repository.CourseScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CourseScheduleService {

    @Autowired
    private CourseScheduleRepository courseScheduleRepository;

    public ApiResponse<List<CourseSchedule>> getAll() {
        return new ApiResponse<>(true, "Data successfully retrieved", courseScheduleRepository.findAll());
    }

    public CourseSchedule findById(String id) {
        return courseScheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course schedule not found"));
    }

    public ApiResponse<CourseSchedule> getById(String id) {
        CourseSchedule courseSchedule = findById(id);
        return new ApiResponse<>(true, "Data successfully retrieved", courseSchedule);
    }

    public ApiResponse<CourseSchedule> create(CourseSchedule courseSchedule) {
        return new ApiResponse<>(true, "Course schedule data has been successfully added", courseScheduleRepository.save(courseSchedule));
    }

    public ApiResponse<CourseSchedule> update(String id, CourseSchedule courseSchedule) {
        findById(id);
        courseSchedule.setId(id);
        return new ApiResponse<>(true, "Course schedule data has been successfully updated", courseScheduleRepository.save(courseSchedule));
    }

    public ApiResponse<CourseSchedule> delete(String id) {
        CourseSchedule courseSchedule = findById(id);
        courseScheduleRepository.delete(courseSchedule);
        return new ApiResponse<>(true, "Course schedule data has been successfully deleted", courseSchedule);
    }
}
