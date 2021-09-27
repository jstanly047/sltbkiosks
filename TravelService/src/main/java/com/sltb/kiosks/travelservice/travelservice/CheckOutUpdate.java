package com.sltb.kiosks.travelservice.travelservice;

import com.sltb.kiosks.travelservice.travelservice.repo.AccountRepo;
import com.sltb.kiosks.travelservice.travelservice.repo.LastCheckInRepo;
import com.sltb.kioskslib.library.model.Account;
import com.sltb.kioskslib.library.model.LastCheckIn;
import com.sltb.kioskslib.library.model.Transaction;
import com.sltb.kioskslib.library.model.kafka.CheckOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class CheckOutUpdate {
    @Autowired
    AccountRepo accountRepo;
    @Autowired
    LastCheckInRepo lastCheckInRepo;

    final Map<String, Map<String, Integer>> charges =
            Map.ofEntries(
                    Map.entry("zone1",
                            Map.ofEntries(
                                    Map.entry("zone1", 10),
                                    Map.entry("zone2", 40),
                                    Map.entry("zone3", 65),
                                    Map.entry("zone4", 85)
                            )
                    ),
                    Map.entry("zone2",
                            Map.ofEntries(
                                    Map.entry("zone1", 40),
                                    Map.entry("zone2", 10),
                                    Map.entry("zone3", 65),
                                    Map.entry("zone4", 70)
                            )
                    ),
                    Map.entry("zone3",
                            Map.ofEntries(
                                    Map.entry("zone1", 65),
                                    Map.entry("zone2", 40),
                                    Map.entry("zone3", 10),
                                    Map.entry("zone4", 40)
                            )
                    ),
                    Map.entry("zone4",
                            Map.ofEntries(
                                    Map.entry("zone1", 85),
                                    Map.entry("zone2", 70),
                                    Map.entry("zone3", 40),
                                    Map.entry("zone4", 10)
                            )
                    )
            );

    private Integer getCharge(String origin, String destination) {
        Map<String, Integer> destMap = charges.get(origin);
        if (destMap == null)
        {
            return 20;
        }

        Integer charge = destMap.get(destination);
        if (charge == null)
        {
            return 20;
        }

        return charge;
    }

    @KafkaListener(topics="${checkout.topic.name}", groupId = "${kakfa.consumer.group}")
    @SendTo
    public CheckOut listenToCheckOut(CheckOut checkOut) throws  InterruptedException{
        Optional<Account> account = accountRepo.findById(checkOut.getAccountid());

        if (account.isPresent() == false)
        {
            checkOut.setReply("Can not find account");
            return checkOut;
        }

        Optional<LastCheckIn> lastCheckInOpt = lastCheckInRepo.findById(checkOut.getAccountid());
        LastCheckIn lastCheckIn = lastCheckInOpt.orElse(new LastCheckIn());
        Integer charge = getCharge(lastCheckIn.getOrigin(), checkOut.getDestination());
        account.get().setBalance(account.get().getBalance() - charge);
        //Transaction txn = new Transaction(charge, checkOut, lastCheckIn, account.get());
        return checkOut;
    }
}
