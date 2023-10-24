package com.fikupnvj.restfulapi.repository;

import com.fikupnvj.restfulapi.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findFirstByToken(String token);
}
