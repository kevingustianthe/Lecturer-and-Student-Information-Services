package com.fikupnvj.restfulapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    private String id;
    private String name;
    private String nim;
    private String classOf;
    private String email;
    private String telephone;
    private String studyProgram;
    private String interest;
    private List<CourseScheduleResponse> courseSchedules;
}
