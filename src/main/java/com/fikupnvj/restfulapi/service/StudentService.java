package com.fikupnvj.restfulapi.service;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.CourseSchedule;
import com.fikupnvj.restfulapi.entity.Student;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.CourseScheduleResponse;
import com.fikupnvj.restfulapi.model.StudentResponse;
import com.fikupnvj.restfulapi.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseScheduleService courseScheduleService;

    public ApiResponse<List<Student>> getAll() {
        return new ApiResponse<>(true, "Data successfully retrieved", studentRepository.findAll());
    }

    public Student findStudentById(String id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student data not found"));
    }

    public ApiResponse<StudentResponse> getMe(Account account) {
        Student student = studentRepository.findByEmail(account.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student data not found"));
        StudentResponse studentResponse = toStudentResponse(student);

        if (student.getAccount() == null) {
            student.setAccount(account);
            studentRepository.save(student);
        }

        return new ApiResponse<>(true, "Data successfully retrieved", studentResponse);
    }

    public ApiResponse<List<CourseScheduleResponse>> getMeCourseSchedule(Account account) {
        Student student = studentRepository.findByEmail(account.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student data not found"));
        List<CourseScheduleResponse> courseSchedules = courseScheduleService.toListCourseScheduleResponse(student.getCourseSchedules());

        return new ApiResponse<>(true, "Data successfully retrieved", courseSchedules);
    }

    public ApiResponse<List<CourseScheduleResponse>> getMeCourseScheduleToday(Account account) {
        Student student = studentRepository.findByEmail(account.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student data not found"));

        List<CourseScheduleResponse> courseSchedulesToday = courseScheduleService
                .toListCourseScheduleResponse(student.getCourseSchedules())
                .stream().filter(schedule -> schedule.getDay() == LocalDate.now().getDayOfWeek()).toList();

        return new ApiResponse<>(true, "Course Schedule Today", courseSchedulesToday);
    }

    public ApiResponse<StudentResponse> getById(String id) {
        Student student = findStudentById(id);
        StudentResponse studentResponse = toStudentResponse(student);

        return new ApiResponse<>(true, "Data successfully retrieved", studentResponse);
    }

    public ApiResponse<List<CourseScheduleResponse>> getStudentCourseSchedule(String id) {
        Student student = findStudentById(id);
        List<CourseScheduleResponse> courseSchedules = courseScheduleService.toListCourseScheduleResponse(student.getCourseSchedules());

        return new ApiResponse<>(true, "Data successfully retrieved", courseSchedules);
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

    public StudentResponse toStudentResponse(Student student) {
        return new StudentResponse(
                student.getId(),
                student.getName(),
                student.getNim(),
                student.getClassOf(),
                student.getEmail(),
                student.getTelephone(),
                student.getStudyProgram(),
                student.getInterest(),
                courseScheduleService.toListCourseScheduleResponse(student.getCourseSchedules())
        );
    }
}
