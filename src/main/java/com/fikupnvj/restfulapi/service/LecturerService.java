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
import org.springframework.data.domain.Sort;
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

    public Lecturer findByEmail(String email) {
        return lecturerRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lecturer data not found"));
    }

    public ApiResponse<LecturerResponse> getMe(Account account) {
        Lecturer lecturer = findByEmail(account.getEmail());

        if (lecturer.getAccount() == null) {
            lecturer.setAccount(account);
            lecturerRepository.save(lecturer);
        }

        lecturerActivityService.updateAllStatus(lecturer.getLecturerActivities());
        LecturerResponse lecturerResponse = toLecturerResponse(lecturer);

        return new ApiResponse<>(true, "Data successfully retrieved", lecturerResponse);
    }

    public ApiResponse<List<LecturerActivityResponse>> getMeLecturerActivity(Account account) {
        Lecturer lecturer = findByEmail(account.getEmail());

        lecturerActivityService.updateAllStatus(lecturer.getLecturerActivities());
        List<LecturerActivityResponse> lecturerActivityResponses = toListLecturerActivityResponse(lecturer.getLecturerActivities());

        return new ApiResponse<>(true, "Data successfully retrieved", lecturerActivityResponses);
    }

    public ApiResponse<List<CourseScheduleResponse>> getMeLecturerCourseSchedule(Account account) {
        Lecturer lecturer = findByEmail(account.getEmail());

        List<CourseScheduleResponse> courseScheduleResponses = courseScheduleService.toListCourseScheduleResponse(lecturer.getCourseSchedules());

        return new ApiResponse<>(true, "Data successfully retrieved", courseScheduleResponses);
    }

    public ApiResponse<LecturerResponse> getById(String id) {
        Lecturer lecturer = findById(id);
        lecturerActivityService.updateAllStatus(lecturer.getLecturerActivities());
        return new ApiResponse<>(true, "Data", toLecturerResponse(lecturer));
    }

    public ApiResponse<List<Lecturer>> getByParam(String name, String studyProgram, String expertise, String sortBy, String order) {
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
        if (!isDuplicate(lecturer)) {
            lecturerRepository.save(lecturer);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lecturer data already exists");
        }
        return new ApiResponse<>(true, "Lecturer data has been successfully added", lecturer);
    }

    public ApiResponse<Lecturer> update(String id, Lecturer lecturer) {
        findById(id);
        if (canUpdate(id, lecturer)) {
            lecturer.setId(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Lecturer data already exists");
        }
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
