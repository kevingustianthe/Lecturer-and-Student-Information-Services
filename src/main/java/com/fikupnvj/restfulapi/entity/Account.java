package com.fikupnvj.restfulapi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_m_account")
public class Account {

    @Id
    private String email;

    @Column(nullable = false)
    private String password;

    private String role;

    private Boolean status;

    @Column(name = "verification_code")
    private String verificationCode;

    private String token;

    @Column(name = "token_expired_at")
    private Long tokenExpiredAt;

    @OneToOne(mappedBy = "account")
    private Student student;

    @OneToOne(mappedBy = "account")
    private Lecturer lecturer;

    @PreRemove
    private void preRemove() {
        if (student.getAccount() != null) student.setAccount(null);
        if (lecturer.getAccount() != null) lecturer.setAccount(null);
    }
}
