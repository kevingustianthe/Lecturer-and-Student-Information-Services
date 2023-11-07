package com.fikupnvj.restfulapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LecturerResponse {
    private String id;
    private String name;
    private String nip;
    private String nidn;
    private String email;
    private String telephone;
    private String studyProgram;
    private List<String> expertise;
    private List<CourseScheduleResponse> courseSchedules;
    private List<LecturerActivityResponse> lecturerActivities;
}
