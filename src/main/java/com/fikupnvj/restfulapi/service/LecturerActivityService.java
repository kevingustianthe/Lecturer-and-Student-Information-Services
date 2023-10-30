package com.fikupnvj.restfulapi.service;

import com.fikupnvj.restfulapi.entity.LecturerActivity;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.repository.LecturerActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class LecturerActivityService {

    @Autowired
    private LecturerActivityRepository lecturerActivityRepository;

    public ApiResponse<List<LecturerActivity>> getAll() {
        return new ApiResponse<>(true, "Data successfully retrieved", lecturerActivityRepository.findAll());
    }

    public LecturerActivity findById(String id) {
        return lecturerActivityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecturer activity not found"));
    }

    public ApiResponse<LecturerActivity> getById(String id) {
        LecturerActivity lecturerActivity = findById(id);
        return new ApiResponse<>(true, "Data successfully retrieved", lecturerActivity);
    }

    public ApiResponse<LecturerActivity> create(LecturerActivity lecturerActivity) {
        if (lecturerActivity.getStartDate().isAfter(lecturerActivity.getEndDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date can't be greater than end date");
        }
        lecturerActivity.setStatus(lecturerActivity);

        return new ApiResponse<>(true, "Lecturer activity data has been successfully added", lecturerActivityRepository.save(lecturerActivity));
    }

    public ApiResponse<LecturerActivity> update(String id, LecturerActivity lecturerActivity) {
        findById(id);
        lecturerActivity.setId(id);
        lecturerActivity.setStatus(lecturerActivity);

        return new ApiResponse<>(true, "Lecturer activity data has been successfully updated", lecturerActivityRepository.save(lecturerActivity));
    }

    public ApiResponse<LecturerActivity> delete(String id) {
        LecturerActivity lecturerActivity = findById(id);
        lecturerActivityRepository.delete(lecturerActivity);
        return new ApiResponse<>(true, "Lecturer activity data has been successfully deleted", lecturerActivity);
    }
}
