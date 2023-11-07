package com.fikupnvj.restfulapi.service;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.Student;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.CourseScheduleResponse;
import com.fikupnvj.restfulapi.model.StudentResponse;
import com.fikupnvj.restfulapi.repository.StudentRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseScheduleService courseScheduleService;

    @Autowired
    private EmailService emailService;

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

    public ApiResponse<List<Student>> getByParam(String name, String nim, String classOf, String studyProgram, String interest, String sortBy, String order) {
        Sort sort = Sort.by(Sort.Order.by(sortBy).with(Sort.Direction.fromString(order)));
        List<Student> students = studentRepository.findAll(sort);

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

    public List<Student> parseExcelImportStudent(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            DataFormatter df = new DataFormatter();
            List<Student> students = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                Student student = new Student();

                int cellIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIndex) {
                        case 1 -> {
                            String name = currentCell.getStringCellValue();
                            student.setName(name);
                        }
                        case 2 -> {
                            String nim = df.formatCellValue(currentCell);
                            student.setNim(nim);
                        }
                        case 3 -> {
                            String classOf = df.formatCellValue(currentCell);
                            student.setClassOf(classOf);
                        }
                        case 4 -> {
                            String email = currentCell.getStringCellValue();
                            if (emailService.checkValidEmail(email)) {
                                student.setEmail(email);
                            } else {
                                student.setEmail("");
                            }
                        }
                        case 5 -> {
                            String telephone = df.formatCellValue(currentCell);
                            student.setTelephone(telephone);
                        }
                        case 6 -> {
                            String studyProgram = currentCell.getStringCellValue();
                            student.setStudyProgram(studyProgram);
                        }
                        case 7 -> {
                            String interest = currentCell.getStringCellValue();
                            student.setInterest(interest);
                        }
                        default -> {
                        }
                    }
                    cellIndex++;
                }

                if (!Objects.equals(student.getName(), "") && !Objects.equals(student.getNim(), "") && !Objects.equals(student.getClassOf(), "") && !Objects.equals(student.getEmail(), "") && !Objects.equals(student.getTelephone(), "") && !Objects.equals(student.getStudyProgram(), "") && !Objects.equals(student.getInterest(), "")) {
                    if (!isDuplicate(student)) {
                        students.add(student);
                    }
                }
            }

            workbook.close();
            return students;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    public ApiResponse<List<Student>> importExcelStudentData(MultipartFile file) {
        List<Student> students;
        try {
            students = parseExcelImportStudent(file.getInputStream());
            if (students.isEmpty()) {
                return new ApiResponse<>(true, "Student data already exists or data is incomplete", students);
            }

            students.forEach(student -> {
                try {
                    create(student);
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
                }
            });

            return new ApiResponse<>(true, "Student data has been successfully added", students);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
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
