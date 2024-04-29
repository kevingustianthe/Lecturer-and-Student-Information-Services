package com.fikupnvj.restfulapi.service;

import com.fikupnvj.restfulapi.entity.LecturerActivity;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.repository.LecturerActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class LecturerActivityService {

    @Autowired
    private LecturerActivityRepository lecturerActivityRepository;

    public ResponseEntity<Object> getAll() {
        List<LecturerActivity> activities = lecturerActivityRepository.findAll();
        updateAllStatus(activities);

        ApiResponse<List<LecturerActivity>> response = new ApiResponse<>(true, "Data successfully retrieved", activities);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public LecturerActivity findById(String id) {
        return lecturerActivityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecturer activity not found"));
    }

    public ResponseEntity<Object> getById(String id) {
        LecturerActivity lecturerActivity = findById(id);
        updateStatus(lecturerActivity);

        ApiResponse<LecturerActivity> response = new ApiResponse<>(true, "Data successfully retrieved", lecturerActivity);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> search(LecturerActivity.Status status, String name, String sortBy, String order) {
        Sort sort = Sort.by(Sort.Order.by(sortBy).with(Sort.Direction.fromString(order)));
        List<LecturerActivity> lecturerActivities = lecturerActivityRepository.findAll(sort);
        if (status != null) {
            lecturerActivities = lecturerActivities.stream().filter(
                    lecturerActivity -> lecturerActivity.getStatus().equals(status)
            ).toList();
        }

        if (!Objects.equals(name, "")) {
            lecturerActivities = lecturerActivities.stream().filter(
                    lecturerActivity -> lecturerActivity.getLecturer().getName().toLowerCase().contains(name.toLowerCase())
            ).toList();
        }

        ApiResponse<List<LecturerActivity>> response = new ApiResponse<>(true, "Data successfully retrieved", lecturerActivities);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> create(LecturerActivity lecturerActivity) {
        validateDate(lecturerActivity);
        lecturerActivity.updateStatus();
        if (isDuplicate(lecturerActivity)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lecturer activity data already exists");
        }

        ApiResponse<LecturerActivity> response = new ApiResponse<>(true, "Lecturer activity data has been successfully added", lecturerActivityRepository.save(lecturerActivity));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> update(String id, LecturerActivity lecturerActivity) {
        findById(id);
        lecturerActivity.setId(id);
        validateDate(lecturerActivity);
        lecturerActivity.updateStatus();
        if (!canUpdate(id, lecturerActivity)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lecturer activity data already exists");
        }

        ApiResponse<LecturerActivity> response = new ApiResponse<>(true, "Lecturer activity data has been successfully updated", lecturerActivityRepository.save(lecturerActivity));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> delete(String id) {
        LecturerActivity lecturerActivity = findById(id);
        lecturerActivityRepository.delete(lecturerActivity);

        ApiResponse<LecturerActivity> response = new ApiResponse<>(true, "Lecturer activity data has been successfully deleted", lecturerActivity);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public void validateDate(LecturerActivity lecturerActivity) {
        if (lecturerActivity.getStartDate().isAfter(lecturerActivity.getEndDate())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Start date can't be greater than end date");
        }
    }

    public void updateStatus(LecturerActivity lecturerActivity) {
        lecturerActivity.updateStatus();
        lecturerActivityRepository.save(lecturerActivity);
    }

    public void updateAllStatus(List<LecturerActivity> activities) {
        for (LecturerActivity activity : activities) {
            updateStatus(activity);
        }
    }

    public boolean isDuplicate(LecturerActivity lecturerActivity) {
        LecturerActivity dbLecturerActivity = lecturerActivityRepository.findByLecturerIdAndDescriptionAndStartDateAndEndDate(
                lecturerActivity.getLecturer().getId(), lecturerActivity.getDescription(), lecturerActivity.getStartDate(), lecturerActivity.getEndDate()
        ).orElse(null);

        return dbLecturerActivity != null;
    }

    public boolean canUpdate(String id, LecturerActivity lecturerActivity) {
        LecturerActivity dbLecturer = findById(id);
        if (dbLecturer.getLecturer().getId().equals(lecturerActivity.getLecturer().getId()) && dbLecturer.getDescription().equals(lecturerActivity.getDescription()) && dbLecturer.getStartDate().equals(lecturerActivity.getStartDate()) && dbLecturer.getEndDate().equals(lecturerActivity.getEndDate())) {
            return true;
        } else return !isDuplicate(lecturerActivity);
    }
}
