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
import java.util.Objects;
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

    public ApiResponse<List<Student>> getByParam(String name, String nim, String classOf, String studyProgram, String interest) {
        List<Student> students = studentRepository.findAll();

        if (!Objects.equals(name, "")) {
            students = students.stream().filter(
                    student -> student.getName().toLowerCase().contains(name.toLowerCase())
            ).toList();
        }

        if (!Objects.equals(nim, "")) {
            students = students.stream().filter(
                    student -> student.getNim().contains(nim)
            ).toList();
        }

        if (!Objects.equals(classOf, "")) {
            students = students.stream().filter(
                    student -> student.getClassOf().equals(classOf)
            ).toList();
        }

        if (!Objects.equals(studyProgram, "")) {
            students = students.stream().filter(
                    student -> student.getStudyProgram().equals(studyProgram)
            ).toList();
        }

        if (!Objects.equals(interest, "")) {
            students = students.stream().filter(
                    student -> student.getInterest().toLowerCase().contains(interest.toLowerCase())
            ).toList();
        }

        return new ApiResponse<>(true, "Data successfully retrieved", students);
    }

    public ApiResponse<List<CourseScheduleResponse>> getStudentCourseSchedule(String id) {
        Student student = findStudentById(id);
        List<CourseScheduleResponse> courseSchedules = courseScheduleService.toListCourseScheduleResponse(student.getCourseSchedules());

        return new ApiResponse<>(true, "Data successfully retrieved", courseSchedules);
    }

    public ApiResponse<Student> create(Student student) {
        if (!isDuplicate(student)) {
            studentRepository.save(student);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student data already exists");
        }
        return new ApiResponse<>(true, "Student data has been successfully added", student);
    }

    public ApiResponse<Student> update(String id, Student student) {
        findStudentById(id);
        if (canUpdate(id, student)) {
            student.setId(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Student data already exists");
        }
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

    public boolean isDuplicate(Student student) {
        Student dbStudent = studentRepository.findFirstByNimOrEmailOrTelephone(student.getNim(), student.getEmail(), student.getTelephone())
                .orElse(null);

        return dbStudent != null;
    }

    public boolean canUpdate(String id, Student student) {
        Student dbStudent = findStudentById(id);

        if (dbStudent.getNim().equals(student.getNim()) && dbStudent.getEmail().equals(student.getEmail()) && dbStudent.getTelephone().equals(student.getTelephone())) {
            return true;
        } else {
            if (!dbStudent.getNim().equals(student.getNim())) {
                if (studentRepository.findByNim(student.getNim()).isPresent()) {
                    return false;
                }
            }
            if (!Objects.equals(dbStudent.getEmail(), student.getEmail())) {
                if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
                    return false;
                }
            }
            if (!Objects.equals(dbStudent.getTelephone(), student.getTelephone())) {
                return studentRepository.findByTelephone(student.getTelephone()).isEmpty();
            }

            return true;
        }
    }
}
