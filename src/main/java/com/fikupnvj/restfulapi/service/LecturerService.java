package com.fikupnvj.restfulapi.service;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.CourseSchedule;
import com.fikupnvj.restfulapi.entity.Lecturer;
import com.fikupnvj.restfulapi.entity.LecturerActivity;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.LecturerActivityResponse;
import com.fikupnvj.restfulapi.model.LecturerCourseScheduleResponse;
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

    public ApiResponse<List<LecturerActivityResponse>> getLecturerActivity(String id) {
        Lecturer lecturer = findById(id);
        List<LecturerActivity> activities = lecturer.getLecturerActivities();

        List<LecturerActivityResponse> lecturerActivities = new ArrayList<>();
        for (LecturerActivity activity : activities) {
            LecturerActivityResponse lecturerActivity = toLecturerActivityResponse(activity);
            lecturerActivities.add(lecturerActivity);
        }

        return new ApiResponse<>(true, "Data", lecturerActivities);
    }

    public ApiResponse<List<LecturerCourseScheduleResponse>> getLecturerCourseSchedule(String id) {
        Lecturer lecturer = findById(id);
        List<CourseSchedule> courseSchedules = lecturer.getCourseSchedules();

        List<LecturerCourseScheduleResponse> lecturerCourseSchedules = new ArrayList<>();
        for (CourseSchedule courseSchedule : courseSchedules) {
            LecturerCourseScheduleResponse lecturerCourseSchedule = toLecturerCourseScheduleResponse(courseSchedule);
            lecturerCourseSchedules.add(lecturerCourseSchedule);
        }

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

    public LecturerActivityResponse toLecturerActivityResponse(LecturerActivity lecturerActivity) {
        LecturerActivityResponse lecturerActivityResponse = new LecturerActivityResponse();

        lecturerActivityResponse.setId(lecturerActivity.getId());
        lecturerActivityResponse.setDescription(lecturerActivity.getDescription());
        lecturerActivityResponse.setStatus(lecturerActivity.getStatus());
        lecturerActivityResponse.setStartDate(lecturerActivity.getStartDate());
        lecturerActivityResponse.setEndDate(lecturerActivity.getEndDate());
        lecturerActivityResponse.setCreatedAt(lecturerActivity.getCreatedAt());
        lecturerActivityResponse.setUpdateAt(lecturerActivity.getUpdateAt());

        return lecturerActivityResponse;
    }

    public LecturerCourseScheduleResponse toLecturerCourseScheduleResponse(CourseSchedule courseSchedule) {
        LecturerCourseScheduleResponse lecturerCourseScheduleResponse = new LecturerCourseScheduleResponse();

        lecturerCourseScheduleResponse.setId(courseSchedule.getId());
        lecturerCourseScheduleResponse.setCourseName(courseSchedule.getCourse().getName());
        lecturerCourseScheduleResponse.setCourseCredit(courseSchedule.getCourse().getCredit());
        lecturerCourseScheduleResponse.setCourseStudyProgram(courseSchedule.getCourse().getStudyProgram());
        lecturerCourseScheduleResponse.setSemester(courseSchedule.getSemester());
        lecturerCourseScheduleResponse.setClassName(courseSchedule.getClassName());
        lecturerCourseScheduleResponse.setRoom(courseSchedule.getRoom());
        lecturerCourseScheduleResponse.setDay(courseSchedule.getDay());
        lecturerCourseScheduleResponse.setStartTime(courseSchedule.getStartTime());
        lecturerCourseScheduleResponse.setEndTime(courseSchedule.getEndTime());

        return lecturerCourseScheduleResponse;
    }
}
