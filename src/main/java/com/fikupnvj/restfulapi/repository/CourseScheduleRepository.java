package com.fikupnvj.restfulapi.repository;

import com.fikupnvj.restfulapi.entity.CourseSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;

@Repository
public interface CourseScheduleRepository extends JpaRepository<CourseSchedule, String> {

    Optional<CourseSchedule> findByAcademicPeriodAndClassNameAndDayAndStartTimeAndEndTimeAndRoom(
           String academicPeriod, String className, DayOfWeek day, LocalTime startTime, LocalTime endTime, String room);
}
