package com.floodresponse.repository;

import com.floodresponse.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByIsActiveTrue();
    List<Alert> findByRegion(String region);
}
