package com.fikupnvj.restfulapi.repository;

import com.fikupnvj.restfulapi.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    Optional<Course> findByNameAndCreditAndSemesterAndStudyProgram(String name, int credit, int semester, String studyProgram);
}
