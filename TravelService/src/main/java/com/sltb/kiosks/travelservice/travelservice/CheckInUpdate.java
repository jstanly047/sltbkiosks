package com.sltb.kiosks.travelservice.travelservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sltb.kiosks.travelservice.travelservice.repo.AccountRepo;
import com.sltb.kiosks.travelservice.travelservice.repo.LastCheckInRepo;
import com.sltb.kioskslib.library.model.Account;
import com.sltb.kioskslib.library.model.LastCheckIn;
import com.sltb.kioskslib.library.model.kafka.CheckIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class CheckInUpdate {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AccountRepo accountRepo;
    @Autowired
    LastCheckInRepo checkinRepo;

    final double MIN_BALANCE = 40.0;

    @KafkaListener(topics="${checkin.topic.name}", groupId = "${kakfa.consumer.group}")
    @SendTo
    public CheckIn listenToCheckIn(CheckIn checkIn) throws  InterruptedException{
        Optional<Account> account = accountRepo.findById(checkIn.getAccountid());

        if (account.isPresent() == false)
        {
            checkIn.setReply("Can not find account");
            return checkIn;
        }

        if (account.get().getBalance() < MIN_BALANCE)
        {
            checkIn.setReply("Don't have enough balance");
            return checkIn;
        }

        Optional<LastCheckIn> checkInDetail = checkinRepo.findById(checkIn.getAccountid());
        LastCheckIn lastCheckIn = checkInDetail.orElse(new LastCheckIn());
        lastCheckIn.setAccountid(checkIn.getAccountid());
        lastCheckIn.setOrigin(checkIn.getOrigin());
        lastCheckIn.setDate(checkIn.getDate());
        checkinRepo.save(lastCheckIn);
        checkIn.setReply("Checkin Success");
        return checkIn;
    }
}
