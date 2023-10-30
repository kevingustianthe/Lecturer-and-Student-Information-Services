package com.fikupnvj.restfulapi.repository;

import com.fikupnvj.restfulapi.entity.LecturerActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LecturerActivityRepository extends JpaRepository<LecturerActivity, String> {
}
