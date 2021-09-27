package com.sltb.kiosks.txnhistory.repo;

import com.sltb.kioskslib.library.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findById(Long id);
}
