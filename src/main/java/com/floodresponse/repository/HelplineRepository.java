package com.floodresponse.repository;

import com.floodresponse.model.Helpline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HelplineRepository extends JpaRepository<Helpline, Long> {
    List<Helpline> findByCategory(String category);
    List<Helpline> findByRegion(String region);
}
