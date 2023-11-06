package com.fikupnvj.restfulapi.repository;

import com.fikupnvj.restfulapi.entity.LecturerActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LecturerActivityRepository extends JpaRepository<LecturerActivity, String> {

    Optional<List<LecturerActivity>> findByStatus(LecturerActivity.Status status);

    Optional<List<LecturerActivity>> findByLecturerNameContains(String name);

    Optional<List<LecturerActivity>> findByStatusAndLecturerNameContains(LecturerActivity.Status status, String name);

    Optional<LecturerActivity> findByLecturerIdAndDescriptionAndStartDateAndEndDate(String lecturerId, String description, LocalDate startDate, LocalDate endDate);
}
