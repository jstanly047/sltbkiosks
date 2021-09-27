package com.kiosks.sltbkiosksadmin.sltbkiosksadminapp.repo;

import com.sltb.kioskslib.library.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepo extends JpaRepository<Admin, String> {
    Optional<Admin> findById(String id);
}
