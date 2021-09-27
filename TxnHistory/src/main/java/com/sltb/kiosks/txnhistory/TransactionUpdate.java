package com.sltb.kiosks.txnhistory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sltb.kiosks.txnhistory.repo.TransactionRepo;
import com.sltb.kioskslib.library.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionUpdate {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    TransactionRepo transactionRepo;


    @KafkaListener(topics="${txn.topic.name}", groupId = "${kakfa.consumer.group}")
    public void listenToCheckIn(Transaction transaction) {
        transactionRepo.save(transaction);
    }
}
