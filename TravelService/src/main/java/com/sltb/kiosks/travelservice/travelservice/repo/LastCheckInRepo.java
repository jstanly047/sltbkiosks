package com.sltb.kiosks.travelservice.travelservice.repo;

import com.sltb.kioskslib.library.model.LastCheckIn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LastCheckInRepo extends JpaRepository<LastCheckIn, Long> {
    Optional<LastCheckIn> findById(Long id);
}
