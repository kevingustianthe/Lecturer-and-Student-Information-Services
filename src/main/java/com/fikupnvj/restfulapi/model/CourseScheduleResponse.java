package com.fikupnvj.restfulapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseScheduleResponse {
    private String id;
    private String courseName;
    private int courseCredit;
    private String courseStudyProgram;
    private String semester;
    private String className;
    private String room;
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;
    private String lecturerName;
    private int totalStudent;
}
