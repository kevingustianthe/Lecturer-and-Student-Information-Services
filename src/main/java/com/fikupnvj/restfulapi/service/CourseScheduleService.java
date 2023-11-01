package com.fikupnvj.restfulapi.service;

import com.fikupnvj.restfulapi.entity.CourseSchedule;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.CourseScheduleResponse;
import com.fikupnvj.restfulapi.repository.CourseScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CourseScheduleService {

    @Autowired
    private CourseScheduleRepository courseScheduleRepository;

    public ApiResponse<List<CourseScheduleResponse>> getAll() {
        List<CourseSchedule> courseSchedules = courseScheduleRepository.findAll();
        List<CourseScheduleResponse> courseScheduleResponses = toListCourseScheduleResponse(courseSchedules);
        return new ApiResponse<>(true, "Data successfully retrieved", courseScheduleResponses);
    }

    public CourseSchedule findById(String id) {
        return courseScheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course schedule not found"));
    }

    public ApiResponse<CourseScheduleResponse> getById(String id) {
        CourseSchedule courseSchedule = findById(id);
        CourseScheduleResponse courseScheduleResponse = toCourseScheduleResponse(courseSchedule);
        return new ApiResponse<>(true, "Data successfully retrieved", courseScheduleResponse);
    }

    public ApiResponse<List<CourseScheduleResponse>> getByParam(
            String courseName,
            int semester,
            String studyProgram,
            String academicPeriod,
            String room,
            String lecturerName) {
        List<CourseSchedule> courseSchedules = courseScheduleRepository.findAll();

        if (!Objects.equals(courseName, "")) {
            courseSchedules = courseSchedules.stream().filter(schedule -> schedule.getCourse().getName().contains(courseName)).toList();
        }

        if (semester != 0) {
            courseSchedules = courseSchedules.stream().filter(schedule -> schedule.getCourse().getSemester() == semester).toList();
        }

        if (!Objects.equals(studyProgram, "")) {
            courseSchedules = courseSchedules.stream().filter(schedule -> Objects.equals(schedule.getCourse().getStudyProgram(), studyProgram)).toList();
        }

        if (!Objects.equals(academicPeriod, "")) {
            courseSchedules = courseSchedules.stream().filter(schedule -> Objects.equals(schedule.getAcademicPeriod(), academicPeriod)).toList();
        }

        if (!Objects.equals(room, "")) {
            courseSchedules = courseSchedules.stream().filter(schedule -> Objects.equals(schedule.getRoom(), room)).toList();
        }

        if (!Objects.equals(lecturerName, "")) {
            courseSchedules = courseSchedules.stream().filter(schedule -> schedule.getLecturer().getName().contains(lecturerName)).toList();
        }

        List<CourseScheduleResponse> courseScheduleResponses = toListCourseScheduleResponse(courseSchedules);

        return new ApiResponse<>(true, "Data successfully retrieved", courseScheduleResponses);
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

    public CourseScheduleResponse toCourseScheduleResponse(CourseSchedule courseSchedule) {
        return new CourseScheduleResponse(
                courseSchedule.getId(),
                courseSchedule.getCourse().getName(),
                courseSchedule.getCourse().getCredit(),
                courseSchedule.getCourse().getSemester(),
                courseSchedule.getCourse().getStudyProgram(),
                courseSchedule.getAcademicPeriod(),
                courseSchedule.getClassName(),
                courseSchedule.getRoom(),
                courseSchedule.getDay(),
                courseSchedule.getStartTime(),
                courseSchedule.getEndTime(),
                courseSchedule.getLecturer().getName(),
                courseSchedule.getStudents().size()
        );
    }

    public List<CourseScheduleResponse> toListCourseScheduleResponse(List<CourseSchedule> courseSchedules) {
        List<CourseScheduleResponse> courseScheduleResponses = new ArrayList<>();
        for (CourseSchedule courseSchedule : courseSchedules) {
            CourseScheduleResponse courseScheduleResponse = toCourseScheduleResponse(courseSchedule);
            courseScheduleResponses.add(courseScheduleResponse);
        }
        return courseScheduleResponses;
    }
}
