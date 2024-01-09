package com.fikupnvj.restfulapi.service;

import com.fikupnvj.restfulapi.entity.CourseSchedule;
import com.fikupnvj.restfulapi.entity.Student;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.CourseScheduleResponse;
import com.fikupnvj.restfulapi.repository.CourseScheduleRepository;
import com.fikupnvj.restfulapi.repository.StudentRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Service
public class CourseScheduleService {

    @Autowired
    private CourseScheduleRepository courseScheduleRepository;

    @Autowired
    private StudentRepository studentRepository;

    public ApiResponse<List<CourseScheduleResponse>> getAll() {
        List<CourseSchedule> courseSchedules = courseScheduleRepository.findAll();
        List<CourseScheduleResponse> courseScheduleResponses = toListCourseScheduleResponse(courseSchedules);
        return new ApiResponse<>(true, "Data successfully retrieved", courseScheduleResponses);
    }

    public CourseSchedule findById(String id) {
        return courseScheduleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course schedule not found"));
    }

    public ApiResponse<CourseSchedule> getById(String id) {
        CourseSchedule courseSchedule = findById(id);
        return new ApiResponse<>(true, "Data successfully retrieved", courseSchedule);
    }

    public ApiResponse<List<CourseScheduleResponse>> search(
            String courseName,
            int semester,
            String studyProgram,
            String academicPeriod,
            String room,
            String lecturerName,
            String sortBy,
            String order) {
        Sort sort = Sort.by(Sort.Order.by(sortBy).with(Sort.Direction.fromString(order)));
        List<CourseSchedule> courseSchedules = courseScheduleRepository.findAll(sort);

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
        if (isDuplicate(courseSchedule)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course schedule data already exists");
        }
        return new ApiResponse<>(true, "Course schedule data has been successfully added", courseScheduleRepository.save(courseSchedule));
    }

    public ApiResponse<CourseSchedule> update(String id, CourseSchedule courseSchedule) {
        findById(id);
        courseSchedule.setId(id);
        if (!canUpdate(id, courseSchedule)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Course schedule data already exists");
        }
        return new ApiResponse<>(true, "Course schedule data has been successfully updated", courseScheduleRepository.save(courseSchedule));
    }

    public ApiResponse<CourseSchedule> updateCourseScheduleStudents(String id, MultipartFile file) {
        CourseSchedule courseSchedule = findById(id);
        List<Student> students = getListStudentsFromExcel(file);

        students.forEach(student -> {
            if (!courseSchedule.getStudents().contains(student)) {
                courseSchedule.getStudents().add(student);
            }
        });
        courseScheduleRepository.save(courseSchedule);

        return new ApiResponse<>(true, "Course schedule students data has been successfully updated", courseSchedule);
    }

    public ApiResponse<CourseSchedule> delete(String id) {
        CourseSchedule courseSchedule = findById(id);
        courseScheduleRepository.delete(courseSchedule);
        return new ApiResponse<>(true, "Course schedule data has been successfully deleted", courseSchedule);
    }

    public List<Student> getListStudentsFromExcel(MultipartFile file) {
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
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
                String name = new String();
                String nim = new String();
                String email = new String();

                int cellIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIndex) {
                        case 1 -> name = currentCell.getStringCellValue();
                        case 2 -> nim = df.formatCellValue(currentCell);
                        case 3 -> email = currentCell.getStringCellValue();
                        default -> {}
                    }
                    cellIndex++;
                }
                studentRepository.findByNameAndNimAndEmail(name, nim, email).ifPresent(students::add);
            }

            workbook.close();
            return students;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
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

    public boolean isDuplicate(CourseSchedule courseSchedule) {
        CourseSchedule dbCourseSchedule = courseScheduleRepository.findByAcademicPeriodAndClassNameAndDayAndStartTimeAndEndTimeAndRoom(
                courseSchedule.getAcademicPeriod(), courseSchedule.getClassName(), courseSchedule.getDay(), courseSchedule.getStartTime(), courseSchedule.getEndTime(), courseSchedule.getRoom())
                .orElse(null);

        return dbCourseSchedule != null;
    }

    public boolean canUpdate(String id, CourseSchedule courseSchedule) {
        CourseSchedule dbCourse = findById(id);
        if (dbCourse.getAcademicPeriod().equals(courseSchedule.getAcademicPeriod()) && dbCourse.getClassName().equals(courseSchedule.getClassName()) && dbCourse.getDay().equals(courseSchedule.getDay()) && dbCourse.getStartTime().equals(courseSchedule.getStartTime()) && dbCourse.getEndTime().equals(courseSchedule.getEndTime()) && dbCourse.getRoom().equals(courseSchedule.getRoom())) {
            return true;
        } else return !isDuplicate(courseSchedule);
    }
}
