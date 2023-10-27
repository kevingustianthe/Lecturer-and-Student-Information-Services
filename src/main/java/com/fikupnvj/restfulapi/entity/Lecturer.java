package com.fikupnvj.restfulapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "tb_m_lecturer")
public class Lecturer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nip;

    @Column(nullable = false, unique = true)
    private String nidn;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String telephone;

    @Column(name = "study_program", nullable = false)
    private String studyProgram;

    @Column(nullable = false)
    private List<String> expertise;

    @JsonBackReference
    @OneToOne
    @JoinColumn(referencedColumnName = "email")
    private Account account;

    @JsonIgnore
    @OneToMany(mappedBy = "lecturer")
    private List<CourseSchedule> courseSchedules;
}
