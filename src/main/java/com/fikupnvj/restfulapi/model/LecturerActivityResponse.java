package com.fikupnvj.restfulapi.model;

import com.fikupnvj.restfulapi.entity.LecturerActivity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LecturerActivityResponse {
    private String id;
    private String description;
    private LecturerActivity.Status status;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
}
