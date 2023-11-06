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

    Optional<Lecturer> findByNip(String nip);

    Optional<Lecturer> findByNidn(String nidn);

    Optional<Lecturer> findByTelephone(String telephone);

    Optional<Lecturer> findFirstByNipOrNidnOrEmailOrTelephone(String nip, String nidn, String email, String telephone);
}
