package com.sltb.kiosks.userauth.repo;
import com.sltb.kioskslib.library.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PassengerRepo extends JpaRepository<Passenger, Long> {
    Optional<Passenger> findById(Long id);
}
