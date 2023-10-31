package com.fikupnvj.restfulapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_m_course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int credit;

    @Column(nullable = false)
    private int semester;

    @Column(name = "study_program", nullable = false)
    private String studyProgram;

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private List<CourseSchedule> courseSchedules;
}
