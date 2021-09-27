package com.sltb.kiosks.travelservice.travelservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sltb.kiosks.travelservice.travelservice.repo.AccountRepo;
import com.sltb.kioskslib.library.model.Account;
import com.sltb.kioskslib.library.model.Transaction;
import com.sltb.kioskslib.library.model.kafka.TopUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TopUpUpdate {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AccountRepo accountRepo;

    @KafkaListener(topics="${topup.topic.name}", groupId = "${kakfa.consumer.group}")
    @SendTo
    public TopUp listenToTopUp(TopUp topUp) throws  InterruptedException{
        Optional<Account> account = accountRepo.findById(topUp.getAccountId());

        if (account.isPresent() == false)
        {
            topUp.setAmount(0);
            topUp.setReply("Can not find account");
            return topUp;
        }

        account.get().setBalance(account.get().getBalance() + topUp.getAmount());
        accountRepo.save(account.get());
        topUp.setReply("Balance " + account.get().getBalance());
        //Transaction txn = new Transaction(topUp, account.get());
        return topUp;
    }
}
