package com.fikupnvj.restfulapi.repository;

import com.fikupnvj.restfulapi.entity.CourseSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseScheduleRepository extends JpaRepository<CourseSchedule, String> {
}
