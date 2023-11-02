package com.fikupnvj.restfulapi.service;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.Lecturer;
import com.fikupnvj.restfulapi.entity.LecturerActivity;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.CourseScheduleResponse;
import com.fikupnvj.restfulapi.model.LecturerActivityResponse;
import com.fikupnvj.restfulapi.model.LecturerResponse;
import com.fikupnvj.restfulapi.repository.LecturerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class LecturerService {

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    private CourseScheduleService courseScheduleService;

    @Autowired
    private LecturerActivityService lecturerActivityService;

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
        lecturerActivityService.updateAllStatus(lecturer.getLecturerActivities());
        return new ApiResponse<>(true, "Data", toLecturerResponse(lecturer));
    }

    public ApiResponse<List<Lecturer>> getByParam(String name, String studyProgram, String expertise) {
        List<Lecturer> lecturers = lecturerRepository.findAll();

        if (!Objects.equals(name, "")) {
            lecturers = lecturers.stream().filter(
                    lecturer -> lecturer.getName().toLowerCase().contains(name.toLowerCase())
            ).toList();
        }

        if (!Objects.equals(studyProgram, "")) {
            lecturers = lecturers.stream().filter(
                    lecturer -> Objects.equals(lecturer.getStudyProgram(), studyProgram)
            ).toList();
        }

        if (!Objects.equals(expertise, "")) {
            lecturers = lecturers.stream().filter(
                    lecturer -> lecturer.getExpertise().stream().anyMatch(
                            exp -> exp.toLowerCase().contains(expertise.toLowerCase())
                    )
            ).toList();
        }

        return new ApiResponse<>(true, "Data successfully retrieved", lecturers);
    }

    public ApiResponse<List<LecturerActivityResponse>> getLecturerActivity(String id) {
        Lecturer lecturer = findById(id);
        lecturerActivityService.updateAllStatus(lecturer.getLecturerActivities());
        List<LecturerActivityResponse> lecturerActivities = toListLecturerActivityResponse(lecturer.getLecturerActivities());

        return new ApiResponse<>(true, "Data", lecturerActivities);
    }

    public ApiResponse<List<CourseScheduleResponse>> getLecturerCourseSchedule(String id) {
        Lecturer lecturer = findById(id);
        List<CourseScheduleResponse> lecturerCourseSchedules = courseScheduleService.toListCourseScheduleResponse(lecturer.getCourseSchedules());

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

    public LecturerResponse toLecturerResponse(Lecturer lecturer) {
        return new LecturerResponse(
                lecturer.getId(),
                lecturer.getName(),
                lecturer.getNip(),
                lecturer.getNidn(),
                lecturer.getEmail(),
                lecturer.getTelephone(),
                lecturer.getStudyProgram(),
                lecturer.getExpertise(),
                courseScheduleService.toListCourseScheduleResponse(lecturer.getCourseSchedules()),
                toListLecturerActivityResponse(lecturer.getLecturerActivities())
        );
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

}
