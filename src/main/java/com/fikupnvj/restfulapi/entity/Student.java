package com.fikupnvj.restfulapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_m_student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String nim;

    @Column(name = "class_of", nullable = false)
    private String classOf;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String telephone;

    @Column(name = "study_program", nullable = false)
    private String studyProgram;

    private String interest;

    @JsonBackReference
    @OneToOne
    @JoinColumn(referencedColumnName = "email")
    private Account account;

    @JsonIgnore
    @ManyToMany(mappedBy = "students")
    private List<CourseSchedule> courseSchedules;
}
