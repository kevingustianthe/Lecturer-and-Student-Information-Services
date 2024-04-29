package com.fikupnvj.restfulapi.service;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.Lecturer;
import com.fikupnvj.restfulapi.entity.LecturerActivity;
import com.fikupnvj.restfulapi.model.ApiResponse;
import com.fikupnvj.restfulapi.model.CourseScheduleResponse;
import com.fikupnvj.restfulapi.model.LecturerActivityResponse;
import com.fikupnvj.restfulapi.model.LecturerResponse;
import com.fikupnvj.restfulapi.repository.LecturerRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
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

    @Autowired
    private EmailService emailService;

    public ResponseEntity<Object> getAll() {
        ApiResponse<List<Lecturer>> response = new ApiResponse<>(true, "Data successfully retrieved", lecturerRepository.findAll());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public Lecturer findById(String id) {
        return lecturerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecturer data not found"));
    }

    public Lecturer findByEmail(String email) {
        return lecturerRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecturer data not found"));
    }

    public ResponseEntity<Object> getMe(Account account) {
        Lecturer lecturer = findByEmail(account.getEmail());

        if (lecturer.getAccount() == null) {
            lecturer.setAccount(account);
            lecturerRepository.save(lecturer);
        }

        lecturerActivityService.updateAllStatus(lecturer.getLecturerActivities());
        LecturerResponse lecturerResponse = toLecturerResponse(lecturer);

        ApiResponse<LecturerResponse> response = new ApiResponse<>(true, "Data successfully retrieved", lecturerResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> getMeLecturerActivity(Account account) {
        Lecturer lecturer = findByEmail(account.getEmail());

        lecturerActivityService.updateAllStatus(lecturer.getLecturerActivities());
        List<LecturerActivityResponse> lecturerActivityResponses = toListLecturerActivityResponse(lecturer.getLecturerActivities());

        ApiResponse<List<LecturerActivityResponse>> response = new ApiResponse<>(true, "Data successfully retrieved", lecturerActivityResponses);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> getMeLecturerCourseSchedule(Account account) {
        Lecturer lecturer = findByEmail(account.getEmail());

        List<CourseScheduleResponse> courseScheduleResponses = courseScheduleService.toListCourseScheduleResponse(lecturer.getCourseSchedules());

        ApiResponse<List<CourseScheduleResponse>> response = new ApiResponse<>(true, "Data successfully retrieved", courseScheduleResponses);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> getById(String id) {
        Lecturer lecturer = findById(id);
        lecturerActivityService.updateAllStatus(lecturer.getLecturerActivities());

        ApiResponse<LecturerResponse> response = new ApiResponse<>(true, "Data", toLecturerResponse(lecturer));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> getByParam(String name, String studyProgram, String expertise, String sortBy, String order) {
        Sort sort = Sort.by(Sort.Order.by(sortBy).with(Sort.Direction.fromString(order)));
        List<Lecturer> lecturers = lecturerRepository.findAll(sort);

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

        ApiResponse<List<Lecturer>> response = new ApiResponse<>(true, "Data successfully retrieved", lecturers);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> getLecturerActivity(String id) {
        Lecturer lecturer = findById(id);
        lecturerActivityService.updateAllStatus(lecturer.getLecturerActivities());
        List<LecturerActivityResponse> lecturerActivities = toListLecturerActivityResponse(lecturer.getLecturerActivities());

        ApiResponse<List<LecturerActivityResponse>> response = new ApiResponse<>(true, "Data", lecturerActivities);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> getLecturerCourseSchedule(String id) {
        Lecturer lecturer = findById(id);
        List<CourseScheduleResponse> lecturerCourseSchedules = courseScheduleService.toListCourseScheduleResponse(lecturer.getCourseSchedules());

        ApiResponse<List<CourseScheduleResponse>> response = new ApiResponse<>(true, "Data", lecturerCourseSchedules);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> create(Lecturer lecturer) {
        if (!isDuplicate(lecturer)) {
            lecturerRepository.save(lecturer);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lecturer data already exists");
        }

        ApiResponse<Lecturer> response = new ApiResponse<>(true, "Lecturer data has been successfully added", lecturer);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> update(String id, Lecturer lecturer) {
        findById(id);
        if (canUpdate(id, lecturer)) {
            lecturer.setId(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lecturer data already exists");
        }

        ApiResponse<Lecturer> response = new ApiResponse<>(true, "Lecturer data has been successfully updated", lecturerRepository.save(lecturer));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Object> delete(String id) {
        Lecturer lecturer = findById(id);
        lecturerRepository.delete(lecturer);

        ApiResponse<Lecturer> response = new ApiResponse<>(true, "Lecturer data has been successfully deleted", lecturer);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public List<Lecturer> parseLecturerDataFromExcel(InputStream is) {
        try {
            Workbook workbook = new XSSFWorkbook(is);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();
            DataFormatter df = new DataFormatter();
            List<Lecturer> lecturers = new ArrayList<>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();
                Lecturer lecturer = new Lecturer();

                int cellIndex = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();
                    switch (cellIndex) {
                        case 1 -> {
                            String name = currentCell.getStringCellValue();
                            lecturer.setName(name);
                        }
                        case 2 -> {
                            String nip = df.formatCellValue(currentCell);
                            lecturer.setNip(nip);
                        }
                        case 3 -> {
                            String nidn = df.formatCellValue(currentCell);
                            lecturer.setNidn(nidn);
                        }
                        case 4 -> {
                            String email = currentCell.getStringCellValue();
                            if (emailService.checkValidEmail(email)) {
                                lecturer.setEmail(email);
                            } else {
                                lecturer.setEmail("");
                            }
                        }
                        case 5 -> {
                            String telephone = df.formatCellValue(currentCell);
                            lecturer.setTelephone(telephone);
                        }
                        case 6 -> {
                            String studyProgram = currentCell.getStringCellValue();
                            lecturer.setStudyProgram(studyProgram);
                        }
                        case 7 -> {
                            List<String> expertise = List.of(currentCell.getStringCellValue().split(", "));
                            lecturer.setExpertise(expertise);
                        }
                        default -> {
                        }
                    }
                    cellIndex++;
                }

                if (!Objects.equals(lecturer.getName(), "") && !Objects.equals(lecturer.getNip(), "") && !Objects.equals(lecturer.getNidn(), "") && !Objects.equals(lecturer.getEmail(), "") && !Objects.equals(lecturer.getTelephone(), "") && !Objects.equals(lecturer.getStudyProgram(), "") && lecturer.getExpertise() != null) {
                    if (!isDuplicate(lecturer)) {
                        lecturers.add(lecturer);
                    }
                }
            }

            workbook.close();
            return lecturers;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    public ResponseEntity<Object> importData(MultipartFile file) {
        List<Lecturer> lecturers;
        try {
            lecturers = parseLecturerDataFromExcel(file.getInputStream());
            if (lecturers.isEmpty()) {
                ApiResponse<List<Lecturer>> response = new ApiResponse<>(true, "Lecturer data already exists or data is incomplete", lecturers);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }

            lecturers.forEach(lecturer -> {
                try {
                    create(lecturer);
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
                }
            });

            ApiResponse<List<Lecturer>> response = new ApiResponse<>(true, "Lecturer data has been successfully added", lecturers);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
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

    public boolean isDuplicate(Lecturer lecturer) {
        Lecturer dbLecturer = lecturerRepository.findFirstByNipOrNidnOrEmailOrTelephone(lecturer.getNip(), lecturer.getNidn(), lecturer.getEmail(), lecturer.getTelephone())
                .orElse(null);

        return dbLecturer != null;
    }

    public boolean canUpdate(String id, Lecturer lecturer) {
        Lecturer dbLecturer = findById(id);

        if (dbLecturer.getNip().equals(lecturer.getNip()) && dbLecturer.getNidn().equals(lecturer.getNidn()) && dbLecturer.getEmail().equals(lecturer.getEmail()) && dbLecturer.getTelephone().equals(lecturer.getTelephone())) {
            return true;
        } else {
            if (!dbLecturer.getNip().equals(lecturer.getNip())) {
                if (lecturerRepository.findByNip(lecturer.getNip()).isPresent()) {
                    return false;
                }
            }
            if (!Objects.equals(dbLecturer.getNidn(), lecturer.getNidn())) {
                if (lecturerRepository.findByNidn(lecturer.getNidn()).isPresent()) {
                    return false;
                }
            }
            if (!Objects.equals(dbLecturer.getEmail(), lecturer.getEmail())) {
                if (lecturerRepository.findByEmail(lecturer.getEmail()).isPresent()) {
                    return false;
                }
            }
            if (!Objects.equals(dbLecturer.getTelephone(), lecturer.getTelephone())) {
                return lecturerRepository.findByTelephone(lecturer.getTelephone()).isEmpty();
            }

            return true;
        }
    }

}
