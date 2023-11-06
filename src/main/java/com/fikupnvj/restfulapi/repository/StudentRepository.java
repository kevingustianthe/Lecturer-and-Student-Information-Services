package com.fikupnvj.restfulapi.repository;

import com.fikupnvj.restfulapi.entity.Account;
import com.fikupnvj.restfulapi.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    Optional<Student> findByAccount(Account account);

    Optional<Student> findByEmail(String email);

    Optional<Student> findByNim(String nim);

    Optional<Student> findByTelephone(String telephone);

    Optional<Student> findFirstByNimOrEmailOrTelephone(String nim, String email, String telephone);
}
