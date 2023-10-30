package com.fikupnvj.restfulapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_tr_lecturer_activity")
public class LecturerActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "lecturer_id", referencedColumnName = "id")
    private Lecturer lecturer;

    private String description;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    private Status status;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    public enum Status {
        Ongoing, Upcoming, Done
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updateAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateAt = LocalDateTime.now();
    }

    public void setStatus(LecturerActivity lecturerActivity) {
        if (lecturerActivity.getStartDate().isAfter(LocalDate.now())) {
            this.status = Status.Upcoming;
        } else if (lecturerActivity.getEndDate().isBefore(LocalDate.now())) {
            this.status = Status.Done;
        } else {
            this.status = Status.Ongoing;
        }
    }
}
