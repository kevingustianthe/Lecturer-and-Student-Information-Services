package com.fikupnvj.restfulapi.service;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.CourseSchedule;
import com.fikupnvj.restfulapi.entity.Lecturer;
import com.fikupnvj.restfulapi.entity.LecturerActivity;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.LecturerActivityResponse;
import com.fikupnvj.restfulapi.model.LecturerCourseScheduleResponse;
import com.fikupnvj.restfulapi.model.LecturerResponse;
import com.fikupnvj.restfulapi.repository.LecturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class LecturerService {

    @Autowired
    private LecturerRepository lecturerRepository;

    public ApiResponse<List<Lecturer>> getAll() {
        return new ApiResponse<>(true, "Data successfully retrieved", lecturerRepository.findAll());
    }

    public Lecturer findById(String id) {
        return lecturerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecturer data not found"));
    }

    public ApiResponse<Lecturer> getMe(Account account) {
        Lecturer lecturer = lecturerRepository.findByEmail(account.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecturer data not found"));

        if (lecturer.getAccount() == null) {
            lecturer.setAccount(account);
            lecturerRepository.save(lecturer);
        }

        return new ApiResponse<>(true, "Data successfully retrieved", lecturer);
    }

    public ApiResponse<LecturerResponse> getById(String id) {
        Lecturer lecturer = findById(id);
        List<LecturerCourseScheduleResponse> lecturerCourseSchedules = toListLecturerCourseScheduleResponse(lecturer.getCourseSchedules());
        List<LecturerActivityResponse> lecturerActivities = toListLecturerActivityResponse(lecturer.getLecturerActivities());

        LecturerResponse lecturerResponse = new LecturerResponse(lecturer, lecturerCourseSchedules, lecturerActivities);
        return new ApiResponse<>(true, "Data", lecturerResponse);
    }

    public ApiResponse<List<LecturerActivityResponse>> getLecturerActivity(String id) {
        Lecturer lecturer = findById(id);
        List<LecturerActivityResponse> lecturerActivities = toListLecturerActivityResponse(lecturer.getLecturerActivities());

        return new ApiResponse<>(true, "Data", lecturerActivities);
    }

    public ApiResponse<List<LecturerCourseScheduleResponse>> getLecturerCourseSchedule(String id) {
        Lecturer lecturer = findById(id);
        List<LecturerCourseScheduleResponse> lecturerCourseSchedules = toListLecturerCourseScheduleResponse(lecturer.getCourseSchedules());

        return new ApiResponse<>(true, "Data", lecturerCourseSchedules);
    }

    public ApiResponse<Lecturer> create(Lecturer lecturer) {
        lecturerRepository.save(lecturer);
        return new ApiResponse<>(true, "Lecturer data has been successfully added", lecturer);
    }

    public ApiResponse<Lecturer> update(String id, Lecturer lecturer) {
        findById(id);
        lecturer.setId(id);
        return new ApiResponse<>(true, "Lecturer data has been successfully updated", lecturerRepository.save(lecturer));
    }

    public ApiResponse<Lecturer> delete(String id) {
        Lecturer lecturer = findById(id);
        lecturerRepository.delete(lecturer);
        return new ApiResponse<>(true, "Lecturer data has been successfully deleted", lecturer);
    }

    public List<LecturerActivityResponse> toListLecturerActivityResponse(List<LecturerActivity> activities) {
        List<LecturerActivityResponse> lecturerActivities = new ArrayList<>();
        for (LecturerActivity activity : activities) {
            LecturerActivityResponse lecturerActivity = new LecturerActivityResponse(
                    activity.getId(),
                    activity.getDescription(),
                    activity.getStatus(),
                    activity.getStartDate(),
                    activity.getEndDate(),
                    activity.getCreatedAt(),
                    activity.getUpdateAt()
            );
            lecturerActivities.add(lecturerActivity);
        }
        return lecturerActivities;
    }

    public List<LecturerCourseScheduleResponse> toListLecturerCourseScheduleResponse(List<CourseSchedule> courseSchedules) {
        List<LecturerCourseScheduleResponse> lecturerCourseSchedules = new ArrayList<>();
        for (CourseSchedule courseSchedule : courseSchedules) {
            LecturerCourseScheduleResponse lecturerCourseSchedule = new LecturerCourseScheduleResponse(
                    courseSchedule.getId(),
                    courseSchedule.getCourse().getName(),
                    courseSchedule.getCourse().getCredit(),
                    courseSchedule.getCourse().getStudyProgram(),
                    courseSchedule.getSemester(),
                    courseSchedule.getClassName(),
                    courseSchedule.getRoom(),
                    courseSchedule.getDay(),
                    courseSchedule.getStartTime(),
                    courseSchedule.getEndTime()
            );
            lecturerCourseSchedules.add(lecturerCourseSchedule);
        }
        return lecturerCourseSchedules;
    }
}
