package com.fikupnvj.restfulapi.model;

import com.fikupnvj.restfulapi.entity.Lecturer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LecturerResponse {
    private Lecturer lecturer;
    private List<LecturerCourseScheduleResponse> lecturerCourseSchedules;
    private List<LecturerActivityResponse> lecturerActivities;
}
