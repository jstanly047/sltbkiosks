package com.kiosks.sltbkiosksadmin.sltbkiosksadminapp.repo;

import com.sltb.kioskslib.library.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {
    Optional<Account> findById(Long id);
}
