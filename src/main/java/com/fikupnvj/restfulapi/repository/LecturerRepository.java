package com.fikupnvj.restfulapi.repository;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LecturerRepository extends JpaRepository<Lecturer, String> {

    Optional<Lecturer> findByAccount(Account account);

    Optional<Lecturer> findByEmail(String email);
}
